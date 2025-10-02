import java.io.*;

%%

STRING	= \"([^\"\\\n]|\\.)*\"
CHAR	= \'([^\'\\\n]|\\.)*\'

%{
	private StringBuilder mlBuf;
	private StringBuilder jdocBuf;
	
	private int singleLine = 0;
	private int multiLine = 0;
	private int javadoc = 0;
	
	public int getSingleLine() { return singleLine; }
	public int getMultiLine() { return multiLine; }
	public int getJavadoc() { return javadoc; }
	
	private int countNonSpace(String s) {
		int c = 0;
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (ch != ' ' && ch != '\t' && ch != '\n' && ch != '\r') c++;
		}
		return c;
	}
%}

%state ML
%state JDOC

%%

<YYINITIAL> {
	{STRING}		{/* Ignora la cadena de literales */}
	{CHAR}			{/* Ignora la caracteres */}
	\n				{/* Consumir y continuar */}
	.				{/* Ignorar */}

	"//" [^\n]*		{	String s = yytext().substring(2);
						singleLine += countNonSpace(s);}

	"/**"			{	jdocBuf = new StringBuilder();
						yybegin(JDOC);}

	"/*"			{	mlBuf = new StringBuilder();
						yybegin(ML);}
}

<JDOC> {
	"*/"		{	javadoc += countNonSpace(jdocBuf.toString());
					jdocBuf = null; 
					yybegin(YYINITIAL);}

	[^*]+		{ jdocBuf.append(yytext()); }
	\*			{ jdocBuf.append(yytext()); }

	<<EOF>>		{	if(jdocBuf != null) {
					javadoc += countNonSpace(jdocBuf.toString());
						jdocBuf = null;}}
}

<ML> {
	"*/"		{	multiLine += countNonSpace(mlBuf.toString());
					mlBuf = null;
					yybegin(YYINITIAL);}

	[^*]+		{ mlBuf.append(yytext()); }
	\*			{ mlBuf.append(yytext()); }

	<<EOF>>		{	if(mlBuf != null) {
					multiLine += countNonSpace(mlBuf.toString());
					mlBuf = null;
				}}
}
