<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" indent="yes" encoding="utf-8" />




<xsl:template match="/menu">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF-8" />
<meta name="author" content="Γκέσος Παύλος (ΣΣΕ 2002)" />
<title><xsl:value-of select="@name"/></title>
</head>

<script>
function menuClick(e) {
	if ('SPAN' == e.nodeName) swap(e);
	else if ('A' == e.nodeName &amp;&amp; e.href == '') swap(e.previousSibling);
}
function swap(e) { e.className = e.className == 'collapse' ? '' : 'collapse'; }
</script>

<style>
	/* menu functionality */
	div.menu div { margin-left: 15px; }
	div.menu span.collapse { background-image: url('expand.gif'); }
	div.menu span:hover { filter: invert(100%); }
	div.menu span { background-image: url('collapse.gif'); background-repeat: no-repeat; padding-left: 15px; position: relative; top: 6px; }
	div.menu span.collapse+a+br+div { display: none; }
	div.menu *:not(span)+a, div.menu a:first-child { margin-left: 15px; }
	/* menu stylish */
	body { background-color: #ffe; font-family: Tahoma; }
	h1 { font-size: 100%; }
	div.menu a { color: black; text-decoration: none; font-size: 90%; padding: 0 1px; }
	div.menu a:hover { border: black 1px solid; background-color: gainsboro; padding: 0; }
	div.menu a:active { border: blue 1px solid; background-color: powderblue; padding: 0; }
</style>

<base target="content" />

<body>

<h1><xsl:value-of select="@name"/></h1>


<div onclick="menuClick(event.target)" class="menu">
<xsl:apply-templates />
</div>

</body>

</html>
</xsl:template>




<xsl:template match="menu">
	<xsl:if test="*"><span class="collapse" /></xsl:if>
	<xsl:element name="a">
		<xsl:if test="@url">
			<xsl:attribute name="href"><xsl:value-of select="@url"/></xsl:attribute>
		</xsl:if>
		<xsl:if test="@icon">
			<xsl:element name="img">
				<xsl:attribute name="src"><xsl:value-of select="@icon"/></xsl:attribute>
			</xsl:element>
			<xsl:text> </xsl:text>
		</xsl:if>
		<xsl:value-of select="@name"/>
	</xsl:element><br/>
<xsl:if test="*"><div><xsl:apply-templates /></div></xsl:if>
</xsl:template>




</xsl:stylesheet>