package com.yang.tvlauncher.bean;


import java.util.List;

/**
 * Created by
 * yangshuang on 2018/11/29.
 */

public class DBInfoBean {

    private List<Integer> allowUpdateversion;
    private List<InfoListBean> infoList;

    public List<Integer> getAllowUpdateversion() {
        return allowUpdateversion;
    }

    public void setAllowUpdateversion(List<Integer> allowUpdateversion) {
        this.allowUpdateversion = allowUpdateversion;
    }

    public List<InfoListBean> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<InfoListBean> infoList) {
        this.infoList = infoList;
    }

    public static class InfoListBean {
        /**
         * version : 1
         * tables : [{"name":"t_home_row","column":[{"name":"rid","type":"INTEGER","allowEmpty":false,"limit":20,"increment":true,"keyType":"PRIMARY","references":{"tname":null,"cname":null}},{"name":"name","type":"TEXT","allowEmpty":false,"limit":20,"increment":false,"keyType":null,"references":{"tname":null,"cname":null}},{"name":"position","type":"INTEGER","allowEmpty":false,"limit":20,"increment":false,"keyType":null,"references":{"tname":null,"cname":null}}]},{"name":"t_category","column":[{"name":"cid","type":"","allowEmpty":false,"limit":20,"increment":true,"keyType":"primary","references":{"tname":null,"cname":null}},{"name":"name","type":"TEXT","allowEmpty":false,"limit":20,"increment":false,"keyType":null,"references":{"tname":null,"cname":null}}]},{"name":"t_apps","column":[{"name":"aid","type":"int","allowEmpty":false,"limit":20,"increment":true,"keyType":"PRIMARY","references":{"tname":null,"cname":null}},{"name":"cid","type":"int","allowEmpty":false,"limit":20,"increment":true,"keyType":"FOREIGN","references":{"tname":"t_category","cname":"cid"}},{"name":"rid","type":"int","allowEmpty":false,"limit":20,"increment":true,"keyType":"FOREIGN","references":{"tname":"t_home_row","cname":"rid"}},{"name":"name","type":"TEXT","allowEmpty":false,"limit":20,"increment":false,"keyType":null,"references":{"tname":null,"cname":null}},{"name":"package","type":"TEXT","allowEmpty":false,"limit":20,"increment":false,"keyType":null,"references":{"tname":null,"cname":null}},{"name":"icon","type":"TEXT","allowEmpty":false,"limit":20,"increment":false,"keyType":null,"references":{"tname":null,"cname":null}}]}]
         */

        private int version;
        private List<TablesBean> tables;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public List<TablesBean> getTables() {
            return tables;
        }

        public void setTables(List<TablesBean> tables) {
            this.tables = tables;
        }

        public static class TablesBean {
            /**
             * name : t_home_row
             * column : [{"name":"rid","type":"INTEGER","allowEmpty":false,"limit":20,"increment":true,"keyType":"PRIMARY","references":{"tname":null,"cname":null}},{"name":"name","type":"TEXT","allowEmpty":false,"limit":20,"increment":false,"keyType":null,"references":{"tname":null,"cname":null}},{"name":"position","type":"INTEGER","allowEmpty":false,"limit":20,"increment":false,"keyType":null,"references":{"tname":null,"cname":null}}]
             */

            private String name;
            private List<ColumnBean> column;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<ColumnBean> getColumn() {
                return column;
            }

            public void setColumn(List<ColumnBean> column) {
                this.column = column;
            }

            public static class ColumnBean {
                /**
                 * name : rid
                 * type : INTEGER
                 * allowEmpty : false
                 * limit : 20
                 * increment : true
                 * keyType : PRIMARY
                 * references : {"tname":null,"cname":null}
                 */

                private String name;
                private String type;
                private boolean allowEmpty;
                private int limit;
                private boolean increment;
                private String keyType;
                private ReferencesBean references;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public boolean isAllowEmpty() {
                    return allowEmpty;
                }

                public void setAllowEmpty(boolean allowEmpty) {
                    this.allowEmpty = allowEmpty;
                }

                public int getLimit() {
                    return limit;
                }

                public void setLimit(int limit) {
                    this.limit = limit;
                }

                public boolean isIncrement() {
                    return increment;
                }

                public void setIncrement(boolean increment) {
                    this.increment = increment;
                }

                public String getKeyType() {
                    return keyType;
                }

                public void setKeyType(String keyType) {
                    this.keyType = keyType;
                }

                public ReferencesBean getReferences() {
                    return references;
                }

                public void setReferences(ReferencesBean references) {
                    this.references = references;
                }

                public static class ReferencesBean {
                    /**
                     * tname : null
                     * cname : null
                     */

                    private Object tname;
                    private Object cname;

                    public Object getTname() {
                        return tname;
                    }

                    public void setTname(Object tname) {
                        this.tname = tname;
                    }

                    public Object getCname() {
                        return cname;
                    }

                    public void setCname(Object cname) {
                        this.cname = cname;
                    }
                }
            }
        }
    }
}
