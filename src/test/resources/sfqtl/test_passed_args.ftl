<#list template_args as it>
    template_args[${it?index}]=${it!"<NULL>"}
<#else>
    none
</#list>