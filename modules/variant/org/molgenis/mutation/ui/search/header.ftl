<!-- this shows a title and border -->
	<div class="formscreen">
		<div class="form_header" id="${screen.getName()}">
		${screen.label}
		</div>
		
		<#--optional: mechanism to show messages-->
		<#list screen.getMessages() as message>
			<#if message.success>
		<p class="successmessage">${message.text}</p>
			<#else>
		<p class="errormessage">${message.text}</p>
			</#if>
		</#list>
		
		<div class="screenbody">
			<div class="screenpadding">
<#assign vo = model>
<#assign geneDTO = model.geneDTO>
<#assign queryParametersVO = vo.queryParametersVO>

<#include "searchForm.ftl">

<a name="results"><hr/></a>
<h3>Search results</h3>
<h4>${vo.header}</h4>