<#assign conn = default_connection()/>
<#assign coll2 = conn.query("select sys.odcivarchar2list('a', 'b', 'c') from dual")[0][0]/>
Execute call with variety of in/out-bind variables.
<#assign
res = conn.call(
"declare\n" +
"  v1 number := :1;\n" +
"  v2 varchar2(200) := :2;\n" +
"  v3 date := :3;\n" +
"  v4 sys.odcivarchar2list := :4;\n" +
"  v5 sys_refcursor;\n" +
"begin\n" +
"  :5 := v1 + 1;\n" +
"  :6 := v2 || 'x';\n" +
"  :7 := add_months(v3, 1);\n" +
"  v4.extend(); v4(v4.last()) := 'd';\n" +
"  :8 := v4;\n" +
"  open v5 for select date'2015-03-01'+rownum dt, level rn from dual connect by rownum <= 5;\n" +
"  :9 := v5;\n" +
"end;",
{"1" : 1, "2" : "abc", "3" : "12.02.2000"?date["dd.MM.yyyy"], "4" : coll2},
{"5" : "NUMERIC", "6" : "VARCHAR", "7" : "DATE", "8" : "ARRAY:SYS.ODCIVARCHAR2LIST",
"9" : "oracle.jdbc.OracleTypes.CURSOR"}
)
/>

Print out bind variables:
{
number : ${res["5"]}
string : ${res["6"]}
date : ${res["7"]?string["dd.MM.yyyy"]}
collection : [<#list res["8"] as i>'${i}'<#sep>, </#list>]
cursor : {
<#list res["9"] as r>
    row#${(r?index+1)?c}: "DT" = ${r.DT?string["dd.MM.yyyy"]} -- ${r.RN}
</#list>
}
}