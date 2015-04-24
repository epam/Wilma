<?xml version="1.0"?><xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:xs="http://www.w3.org/2001/XMLSchema"
xmlns:fn="http://www.w3.org/2005/xpath-functions"
version="2.0">
	<xsl:output method="xml" indent="yes" encoding="UTF-8"/>

	<!-- parameters converted from the request -->
    <xsl:param name="request" />
	<xsl:param name="id" select="string($request//stuff//@exampleID)"/>

	<!-- copy all attributes (@*) and elements (node()) -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<!-- replace a value in the template -->
	<xsl:template match="text()[.='TO_REPLACE_TEXT']">
		<xsl:value-of select="$id" />
		<xsl:apply-templates select="@*|node()"/>
	</xsl:template>

</xsl:stylesheet>








