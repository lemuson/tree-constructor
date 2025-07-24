import java.util.ArrayList;

public final class TreeNode
{
    public String value;
    public ArrayList<TreeNode> children;

    public TreeNode(String value)
    {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public ArrayList<TreeNode> getChildren()
    {
        return children;
    }
    public String getValue()
    {
        return value;
    }
}