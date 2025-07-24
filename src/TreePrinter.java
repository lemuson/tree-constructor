import java.util.*;
import java.util.function.Function;

public final class TreePrinter<Node> {
    private static final Map<Character, Character> addBranch = Map.of(
            '─', '┴',
            '┬', '┼',
            '┌', '├',
            '┐', '┤'
    );

    private final Function<Node, List<Node>> getChildren;
    private final Function<Node, String> getNodeVal;

    public TreePrinter(Function<Node, List<Node>> getChildren, Function<Node, String> getVal) {
        this.getChildren = getChildren;
        this.getNodeVal = getVal;
    }

    //Строковое представление дерева, начиная с корня
    public String display(Node node) {
        return toStr(node);
    }

    //Вывод узла без глубины (для рекурсии)
    public String toStr(Node node) {
        return toStr(node, 0);
    }

    //Рекурсивное построение дерева с учетом глубины
    public String toStr(Node node, int depth) {
        String[][] res = treeToStr(node, depth);
        var str = new StringBuilder();
        for (var line : res) {
            for (var x : line)
                str.append(x);
            str.append("\n");
        }
        str.deleteCharAt(str.length() - 1);
        return str.toString();
    }

    //Преобразование значения узла в двумерный массив строк
    private String[][] getVal(Node node) {
        var stVal = this.getNodeVal.apply(node);

        if (!stVal.contains("\n"))
            return new String[][]{ new String[]{stVal} };

        var lstVal = stVal.split("\n");
        var longest = 0;
        for (var item : lstVal) {
            longest = Math.max(item.length(), longest);
        }

        var res = new String[lstVal.length][];
        for (int i = 0; i < lstVal.length; i++) {
            res[i] = new String[]{lstVal[i] + " ".repeat(longest - lstVal[i].length())};
        }
        return res;
    }

    //Удаление null-элементов из списка узлов
    private LinkedList<Node> removeNull(List<Node> list) {
        var res = new LinkedList<Node>();
        for (var node : list) {
            if (node == null)
                continue;
            res.addLast(node);
        }
        return res;
    }

    //Представление дерева в виде двумерного массива строк
    private String[][] treeToStr(Node node, int depth) {
        var val = getVal(node);
        var children = this.getChildren.apply(node);
        children = removeNull(children);

        if (children.isEmpty()) {
            return new String[][]{ new String[]{"[" + val[0][0] + "]"} };
        }

        var toPrint = new ArrayList<ArrayList<String>>();
        toPrint.add(new ArrayList<>());
        var spacing_count = 0;

        for (var child : children) {
            var childPrint = treeToStr(child, depth + 1);
            for (int l = 0; l < childPrint.length; l++) {
                var line = childPrint[l];
                if (l + 1 >= toPrint.size())
                    toPrint.add(new ArrayList<>());

                if (l == 0) {
                    var lineLen = lenJoin(List.of(line));
                    var middleOfChild = lineLen - (int) Math.ceil(line[line.length - 1].length() / 2d);
                    var toPrint0Len = lenJoin(toPrint.getFirst());
                    toPrint.getFirst().add(" ".repeat(spacing_count - toPrint0Len + middleOfChild) + "┬");
                }

                var toPrintNxtLen = lenJoin(toPrint.get(l + 1));
                toPrint.get(l + 1).add(" ".repeat(spacing_count - toPrintNxtLen));
                toPrint.get(l + 1).addAll(List.of(line));
            }

            spacing_count = 0;
            for (var item : toPrint) {
                spacing_count = Math.max(lenJoin(item), spacing_count);
            }
            spacing_count++;
        }

        int pipePos;
        if (toPrint.getFirst().size() != 1) {
            var newLines = String.join("", toPrint.getFirst());
            var spaceBefore = newLines.length() - (newLines = newLines.trim()).length();
            int lenOfTrimmed = newLines.length();
            newLines = " ".repeat(spaceBefore) +
                    "┌" + newLines.substring(1, newLines.length() - 1).replace(' ', '─') + "┐";
            var middle = newLines.length() - (int) Math.ceil(lenOfTrimmed / 2d);
            pipePos = middle;

            var newCh = addBranch.get(newLines.charAt(middle));
            newLines = newLines.substring(0, middle) + newCh + newLines.substring(middle + 1);
            var al = new ArrayList<String>();
            al.add(newLines);
            toPrint.set(0, al);
        } else {
            toPrint.getFirst().set(0, toPrint.getFirst().getFirst().substring(0, toPrint.getFirst().getFirst().length() - 1) + '│');
            pipePos = toPrint.getFirst().getFirst().length() - 1;
        }

        var spacing = "";
        if (val[0][0].length() < pipePos * 2)
            spacing = " ".repeat(pipePos - (int) Math.ceil(val[0][0].length() / 2d));

        val = new String[][]{new String[]{spacing, "[" + val[0][0] + "]"}};

        var asArr = new String[val.length + toPrint.size()][];

        int row = 0;
        for (var item : val) {
            asArr[row] = new String[item.length];
            System.arraycopy(item, 0, asArr[row], 0, item.length);
            row++;
        }
        for (var item : toPrint) {
            asArr[row] = new String[item.size()];
            int i = 0;
            for (var x : item)
                asArr[row][i++] = x;
            row++;
        }

        return asArr;
    }

    //Суммарная длина строк в коллекции
    private int lenJoin(Collection<String> lst) {
        return String.join("", lst).length();
    }
}
