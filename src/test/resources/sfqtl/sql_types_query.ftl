<#-- Execute query with variety of in-bind variables and fetch them back. -->
<#assign conn = default_connection()/>
<#assign coll = conn.query("select sys.odcinumberlist(1,2,3) from dual")[0][0]/>
<#assign clob = conn.query("select to_clob('текст для Клоба') from dual")[0][0]/>
<#assign struct = conn.query("select sys.odciobject('goose', 'гусь') from dual")[0][0]/>
<#assign
res =
conn.query(
"select :1 byte, :2 shrt, :3 int, :4 lngint, :5 flt, :6 dbl, :7 bigdec, " +
" :8 + 1/7 dt, :9 tmstmp, :10 str, :11 bool, :12 coll, :13 struct, :14 clob from dual",
[
1?byte, 1?short, 1?int, 1?long, 1.2?float, 1.2?double, 1.56,
"31.03.2055"?date["dd.MM.yyyy"], "31.03.2012 17:23:39.544"?datetime["dd.MM.yyyy HH:mm:ss.SSS"],
"текст по русски", true, coll, struct, clob
]
)
/>
Print columns and their corresponding SQL types:
{
<#list res.metaData as colMeta>
    ${colMeta.columnName} : ${colMeta.columnTypeName}
</#list>
}

Print passed types and returned values.
<#assign r = res[0]/>
{
byte : ${r.BYTE?c}
short : ${r.SHRT?c}
int : ${r.INT?c}
longint : ${r.LNGINT?c}
float : ${r.FLT?c}
double : ${r.DBL?c}
numeric : ${r.BIGDEC?c}
date : ${r.DT?string["dd.MM.yyyy HH:mm:ss"]}
timestamp : ${r.TMSTMP}
timestamp is datetime? - ${r.TMSTMP?is_unknown_date_like?c}
timestamp is string? - ${r.TMSTMP?is_string?c}
string : ${r.STR}
boolean : ${r.BOOL?c}
boolean is int? - ${r.BOOL?is_number?c}
collection = [<#list r.COLL as i>${i}<#sep>, </#list>]
struct = {<#list r.STRUCT as i>field#${(i?index+1)?c} : "${i}"<#sep>, </#list>}
clob = ${r.CLOB}
}