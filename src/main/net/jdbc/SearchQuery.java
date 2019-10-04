package net.jdbc;

import java.util.List;
//describes the query string that will be formed from the parameter list, and placed in StringBuilder
public class SearchQuery {
    private StringBuilder sql;
    private List<Object> params;

    public SearchQuery() {
        super();
    }

    public SearchQuery(StringBuilder sql, List<Object> params) {
        super();
        this.sql = sql;
        this.params = params;
    }

    public StringBuilder getSql() {
        return sql;
    }

    public void setSql(StringBuilder sql) {
        this.sql = sql;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }
}
