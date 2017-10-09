package com.wangrg.java_lib.java_program.video_download.bean;

import java.util.List;

/**
 * by 王荣俊 on 2016/9/25.
 */
public class ImoocBugInfo {

    /**
     * result : 0
     * data : {"result":{"mid":9918,"mpath":["http://v2.mukewang.com/393ecedb-9729-45db-84dc-61acdc096922/L.mp4?auth_key=1474811025-0-0-9be318d97f6098ab668324e57e9b84c7","http://v2.mukewang.com/393ecedb-9729-45db-84dc-61acdc096922/M.mp4?auth_key=1474811025-0-0-cd0625b430bd6f6349fedfd309674a93","http://v2.mukewang.com/393ecedb-9729-45db-84dc-61acdc096922/H.mp4?auth_key=1474811025-0-0-429e8607a94d86c1b849b17f05367ee5"],"cpid":"2571","name":"下期课程简介","time":0,"practise":[]}}
     * msg : 成功
     */

    private int result;
    /**
     * result : {"mid":9918,"mpath":["http://v2.mukewang.com/393ecedb-9729-45db-84dc-61acdc096922/L.mp4?auth_key=1474811025-0-0-9be318d97f6098ab668324e57e9b84c7","http://v2.mukewang.com/393ecedb-9729-45db-84dc-61acdc096922/M.mp4?auth_key=1474811025-0-0-cd0625b430bd6f6349fedfd309674a93","http://v2.mukewang.com/393ecedb-9729-45db-84dc-61acdc096922/H.mp4?auth_key=1474811025-0-0-429e8607a94d86c1b849b17f05367ee5"],"cpid":"2571","name":"下期课程简介","time":0,"practise":[]}
     */
    private DataBean data;
    private String msg;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * mid : 9918
         * mpath : ["http://v2.mukewang.com/393ecedb-9729-45db-84dc-61acdc096922/L.mp4?auth_key=1474811025-0-0-9be318d97f6098ab668324e57e9b84c7","http://v2.mukewang.com/393ecedb-9729-45db-84dc-61acdc096922/M.mp4?auth_key=1474811025-0-0-cd0625b430bd6f6349fedfd309674a93","http://v2.mukewang.com/393ecedb-9729-45db-84dc-61acdc096922/H.mp4?auth_key=1474811025-0-0-429e8607a94d86c1b849b17f05367ee5"]
         * cpid : 2571
         * name : 下期课程简介
         * time : 0
         * practise : []
         */

        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            private int mid;
            private String cpid;
            private String name;
            private int time;
            private List<String> mpath;
            private List<?> practise;

            public int getMid() {
                return mid;
            }

            public void setMid(int mid) {
                this.mid = mid;
            }

            public String getCpid() {
                return cpid;
            }

            public void setCpid(String cpid) {
                this.cpid = cpid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }

            public List<String> getMpath() {
                return mpath;
            }

            public void setMpath(List<String> mpath) {
                this.mpath = mpath;
            }

            public List<?> getPractise() {
                return practise;
            }

            public void setPractise(List<?> practise) {
                this.practise = practise;
            }
        }
    }
}
