import javax.swing.*;
import java.awt.*;

public final class UserInterface extends JFrame
{
    public final JTextField inputTree;
    public final JTextArea outputTree;
    public final JButton buttonStartFunction;
    public final JScrollPane scrollOutputTree;

    public UserInterface()
    {
        super("Построение дерева");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        setLayout(new BorderLayout());

        //Ввод строки для построения дерева
        inputTree = new JTextField();
        inputTree.setFont(new Font("Arial", Font.PLAIN, 16));
        inputTree.setPreferredSize(new Dimension(0, 30));

        //Кнопка для построения дерева
        buttonStartFunction = new JButton("Построить");
        buttonStartFunction.addActionListener(_ -> buttonStartFunction_Click());
        buttonStartFunction.setFont(new Font("Arial", Font.PLAIN, 14));

        //Вывод дерева
        outputTree = new JTextArea();
        scrollOutputTree = new JScrollPane(outputTree);
        outputTree.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputTree.setEditable(false);

        //Закрепление элементов
        GridBagConstraints layoutContainer = new GridBagConstraints();
        JPanel inputPanel = new JPanel(new GridBagLayout());

        layoutContainer.insets = new Insets(5, 5, 5, 5);
        layoutContainer.fill = GridBagConstraints.HORIZONTAL;
        layoutContainer.weightx = 1.0;
        layoutContainer.gridx = 0;
        layoutContainer.gridy = 0;
        inputPanel.add(inputTree, layoutContainer);
        layoutContainer.weightx = 0;
        layoutContainer.gridx = 1;
        inputPanel.add(buttonStartFunction, layoutContainer);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollOutputTree, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    //Нажатие на кнопку построения дерева
    private void buttonStartFunction_Click()
    {
        String treeString = inputTree.getText();
        outputTree.setText("");

        TreeNode tree = TreeBuilder.createNode(treeString);
        outputTree.setText(TreeBuilder.drawTree(tree));
    }
}
