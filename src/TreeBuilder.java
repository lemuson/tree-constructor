public final class TreeBuilder
{
    //Создание дерева
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

    //Вывод дерева
    public static String drawTree(TreeNode node)
    {
        TreePrinter<TreeNode> printer = new TreePrinter<>(TreeNode::getChildren, TreeNode::getValue);
        return printer.display(node);
    }
}