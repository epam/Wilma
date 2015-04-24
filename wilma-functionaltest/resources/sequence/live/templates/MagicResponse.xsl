<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:xs="http://www.w3.org/2001/XMLSchema"
xmlns:fn="http://www.w3.org/2005/xpath-functions"
version="2.0">
	<xsl:output method="xml" indent="yes" encoding="UTF-8"/>
	<!-- parameters handed over from code -->
	<xsl:param name="ImportantResponse"/>
	<xsl:param name="myFavoriteString"  select="$ImportantResponse//my_favorite_string"/>

	<!-- copy all attributes (@*) and elements (node()) -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<!-- replace a value in the template -->
	<xsl:template match="//*[local-name()='MyFavoriteString']/text()[.='SOMETHING']">
		<xsl:value-of select="$myFavoriteString" />
		<xsl:apply-templates select="@*|node()"/>
	</xsl:template>

</xsl:stylesheet>








