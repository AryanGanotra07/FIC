package com.aryanganotra.ficsrcc.StockService;

import java.util.List;

import javax.xml.transform.Result;

public class StockResultModel {
    private ResultSetClass ResultSet;

    public ResultSetClass getResultSet() {
        return ResultSet;
    }

    public void setResultSet(ResultSetClass resultSet) {
        ResultSet = resultSet;
    }

    public StockResultModel(ResultSetClass ResultSet){
        this.ResultSet=ResultSet;
    }

    public static class ResultSetClass{
        private List<Results> Result;

        public List<Results> getResult() {
            return Result;
        }

        public void setResult(List<Results> result) {
            Result = result;
        }

        public ResultSetClass(List<Results> Result){
            this.Result=Result;
        }

        public static class Results{
            private String symbol;
            private String name;
            private String exchDisp;

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getExchDisp() {
                return exchDisp;
            }

            public void setExchDisp(String exchDisp) {
                this.exchDisp = exchDisp;
            }

            public Results(String symbol, String name, String exchDisp){
                this.symbol=symbol;
                this.name=name;
                this.exchDisp=exchDisp;
            }


        }


    }


}
