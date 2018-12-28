package cf.bean;

import java.io.Serializable;

public class PageBean implements Serializable{

	private static final long serialVersionUID = -1251650100823408898L;
	private int pageIndex;
	private int pageSize;
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	@Override
	public String toString() {
		return "PageBean [pageIndex=" + pageIndex + ", pageSize=" + pageSize + "]";
	}
	
	
}
