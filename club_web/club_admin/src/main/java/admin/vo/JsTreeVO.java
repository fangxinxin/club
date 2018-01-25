package admin.vo;

/**
 * 前后端交互信息类，权限分配
 * Created by ds.
 */
public class JsTreeVO {
/*节点的可选格式(需要id和父节点)
{
    id          : "string" // required
    parent      : "string" // required
    text        : "string" // node text
    icon        : "string" // string for custom
    state       : {
                    opened      : boolean  // is the node open
                    disabled    : boolean  // is the node disabled
                    selected    : boolean  // is the node selected
                  },
    li_attr     : {}  // attributes for the generated LI node
    a_attr      : {}  // attributes for the generated A node
}*/

    private String id;
    private String parent;
    private String text;
    private String icon;
    private State  state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }



}
