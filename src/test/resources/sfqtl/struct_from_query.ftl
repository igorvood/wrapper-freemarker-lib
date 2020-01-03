Fetch SQL structure (object type) from query.
<#assign struct = default_connection().query("select sys.odciobject('goose', 'гусь') from dual")[0][0]/>
Print SQL structure: {<#list struct as i>field#${(i?index+1)?c} : "${i}"<#sep>, </#list>}
SQL structure type: ${struct.SQLTypeName}