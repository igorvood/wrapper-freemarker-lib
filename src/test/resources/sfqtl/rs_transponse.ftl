<#assign res = default_connection().query("select rownum n, 'row_' || rownum label from dual connect by level <= 5")/>
Print transposed result:
{
<#list res.transpose() as col>
    ${res.metaData[col?index].columnName} : [<#list col as val>${val}<#sep>, </#list>]
</#list>
}