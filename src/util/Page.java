package util;

public class Page {
    int start;
    int count;
    int total;
    String param;

    public Page(int start, int count) {
        super();
        this.start = start;
        this.count = count;
    }

    public boolean isHasPreviouse() {
        if (start == 0)
            return false;
        return true;
    }

    public int getTotalPage() {
        int totalPage;
        if (0 == total % count)
            totalPage = total / count;
        else
            totalPage = total / count + 1;
        if (0 == totalPage)
            totalPage = 1;
        return totalPage;
    }

    public int getLast() {
        int last = total - 1;
        last = Math.max(0, last);
        last = last / count * count;
        return last;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
