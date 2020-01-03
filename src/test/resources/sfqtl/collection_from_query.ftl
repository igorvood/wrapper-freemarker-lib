Fetch SQL collection from query.
<#assign coll = default_connection().query("select sys.odcinumberlist(3, null, 1, 7, -127, 42) from dual")[0][0]/>
Print SQL collection: [<#list coll as i>${i!'<NULL>'}<#sep> _+~ </#list>]
SQL collection type: ${coll.SQLTypeName}
SQL collection base type: ${coll.baseTypeName}
