/*******************************************************************************
 *Copyright (C) 2017 TU Graz.
 *All rights reserved. This program and the accompanying materials
 *are made available under the terms of the Eclipse Public License v1.0
 *which accompanies this distribution, and is available at
 *http://www.eclipse.org/legal/epl-v10.html
 *
 *Contributors:
 *    TU Graz - initial API and implementation
 ******************************************************************************/
var a=null;
PR.registerLangHandler(PR.createSimpleLexer([["str",/^"(?:[^\n\r"\\]|\\.)*(?:"|$)/,a,'"'],["pln",/^\s+/,a," \r\n\t\u00a0"]],[["com",/^REM[^\n\r]*/,a],["kwd",/^\b(?:AND|CLOSE|CLR|CMD|CONT|DATA|DEF ?FN|DIM|END|FOR|GET|GOSUB|GOTO|IF|INPUT|LET|LIST|LOAD|NEW|NEXT|NOT|ON|OPEN|OR|POKE|PRINT|READ|RESTORE|RETURN|RUN|SAVE|STEP|STOP|SYS|THEN|TO|VERIFY|WAIT)\b/,a],["pln",/^[a-z][^\W_]?(?:\$|%)?/i,a],["lit",/^(?:\d+(?:\.\d*)?|\.\d+)(?:e[+-]?\d+)?/i,a,"0123456789"],["pun",
/^.[^\s\w"$%.]*/,a]]),["basic","cbm"]);
