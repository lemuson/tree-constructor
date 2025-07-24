## Построекие текстового представления дерева

Узлы дерева задаются в текстовом виде следующим образом: `(a, b, (c, d...)...)`. Каждый узел дерева описывается в круглых скобках, где первый элемент - вершина, остальные - потомки.

#### Описание работы
#### TreeBuilder
Класс `TreeBuilder` содержит методы для создания и отображения дерева типа `TreeNode` из текстового представления. Метод `TreeNode createNode(String input)` разбирает строку с вложенной структурой, рекурсивно создавая объекты `TreeNode` с детьми.
Метод `String drawTree(TreeNode node)` использует экземпляр класса `TreePrinter` для преобразования дерева в строковое графическое представление.
<details> <summary>TreeNode createNode(String input)</summary>
  
```Java
public static TreeNode createNode(String input)
{
    int idx = input.indexOf(',');
    if (idx == -1)
        return new TreeNode(input.replace("(", "").replace(")", ""));

    TreeNode node = new TreeNode(input.substring(0, idx).trim().replace("(", "").replace(")", ""));//Главный узел
    int count = 0;
    int start = idx + 1;
    for (int i = idx + 1; i < input.length(); i++)
    {
        char c = input.charAt(i);
        if (c == '(') count++;
        else if (c == ')') count--;

        if ((c == ',' && count == 0) || i == input.length() - 1)
        {
            node.children.add(createNode(input.substring(start, i).trim()));
            start = i + 1;
        }
    }
    return node;
}
```
</details>

<details> <summary>String drawTree(TreeNode node))</summary>
  
```Java
public static String drawTree(TreeNode node)
{
    TreePrinter<TreeNode> printer = new TreePrinter<>(TreeNode::getChildren, TreeNode::getValue);
    return printer.display(node);
}
```
</details>

#### TreeNode
Класс `TreeNode` представляет узел дерева и содержит строковое поле `value` для хранения значения узла и список `children` типа `ArrayList<TreeNode>`, в котором хранятся дочерние узлы. Конструктор принимает строку для инициализации значения узла и создает пустой список детей. Методы `getChildren()` и `getValue()` возвращают соответственно список дочерних узлов и значение текущего узла.

#### TreePrinter
Класс `TreePrinter<Node>` реализует логику преобразования дерева с произвольным типом узлов в строковое псевдографическое горизонтальное представление. Он принимает две функции: получения детей узла `(Function<Node, List<Node>>)` и получения строки значения узла `(Function<Node, String>)`. 
Основной метод `String display(Node node)` запускает процесс генерации строки, отображающей дерево. Внутри используется рекурсивный подход: каждое поддерево преобразуется в двумерный массив строк, который затем выравнивается и соединяется с другими поддеревьями при помощи `String[][] treeToStr(Node node, int depth)`.
<details> <summary>String[][] treeToStr(Node node, int depth)</summary>

```Java
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
```
</details>

#### UserInterface
Класс `UserInterface` представляет собой графический интерфейс на базе `JFrame` для построения и отображения дерева При нажатии на кнопку вызывается метод `buttonStartFunction_Click()`, который получает введённый текст, преобразует его в дерево с помощью методов класса `TreeBuilder` и отображает визуальное представление дерева в текстовой области. Интерфейс оформлен с использованием `BorderLayout` и панели с `GridBagLayout` для удобного размещения элементов с адаптацией под размер окна.
<details> <summary>buttonStartFunction_Click()</summary>
  
  ```Java
  private void buttonStartFunction_Click()
  {
      String treeString = inputTree.getText();
      outputTree.setText("");

      TreeNode tree = TreeBuilder.createNode(treeString);
      outputTree.setText(TreeBuilder.drawTree(tree));
    }
  ```
</details>

#### Примеры работы 
<img width="359" height="200" alt="image" src="https://github.com/user-attachments/assets/a50ce4c5-5cf0-4104-89e5-bc4e21483834" />
<img width="359" height="200" alt="image" src="https://github.com/user-attachments/assets/e303b220-3ed1-4ad3-ad89-1f2260fa102b" />
<img width="359" height="200" alt="image" src="https://github.com/user-attachments/assets/a3ae3b14-5481-457e-a728-e25c83c22bc9" />

