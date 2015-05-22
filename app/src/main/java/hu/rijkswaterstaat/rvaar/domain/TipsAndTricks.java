package hu.rijkswaterstaat.rvaar.domain;

/**
 * Created by Haydar on 30-04-15.
 */
public class TipsAndTricks {
    protected String headerName;
    protected String content;
    private int id;

    public TipsAndTricks() {
    }

    public TipsAndTricks(String headerName, String content) {
        super();
        this.headerName = headerName;
        this.content = content;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeaderName() {

        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }


    @Override
    public String toString() {
        return "TipsAndTricks{" +
                "id=" + id +
                ", headerName='" + headerName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}