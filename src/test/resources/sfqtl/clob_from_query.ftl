Fetch CLOB from query.
<#assign clob = default_connection().query("select to_clob('loooong text' || chr(10) || ' русский текст') from dual")[0][0]/>
<#assign clob1 = default_connection().query("select to_clob('loooong text1' || chr(10) || ' русский текст1') from dual")[0][0]/>
Print CLOB: "${clob}"
CLOB length: ${clob?length}
Print CLOB1: "${clob1}"
CLOB length1: ${clob1?length}
