Fetch CLOB from query.
<#assign clob = default_connection().query("select to_clob('loooong text' || chr(10) || ' русский текст') from dual")[0][0]/>
Print CLOB: "${clob}"
CLOB length: ${clob?length}
