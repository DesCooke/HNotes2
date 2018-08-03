package com.example.cooked.hnotes2.Database;

public class TableBase {
    public String HandleSingleQuotes(String argString)
    {
        return(argString.replace("'", "''"));
    }
}
