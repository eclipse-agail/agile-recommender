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
PR.registerLangHandler(PR.createSimpleLexer([["str",/^'(?:[^\n\r'\\]|\\.)*(?:'|$)/,a,"'"],["pln",/^\s+/,a," \r\n\t\u00a0"]],[["com",/^\(\*[\S\s]*?(?:\*\)|$)|^{[\S\s]*?(?:}|$)/,a],["kwd",/^(?:absolute|and|array|asm|assembler|begin|case|const|constructor|destructor|div|do|downto|else|end|external|for|forward|function|goto|if|implementation|in|inline|interface|interrupt|label|mod|not|object|of|or|packed|procedure|program|record|repeat|set|shl|shr|then|to|type|unit|until|uses|var|virtual|while|with|xor)\b/i,a],
["lit",/^(?:true|false|self|nil)/i,a],["pln",/^[a-z][^\W_]*/i,a],["lit",/^(?:\$[\da-f]+|(?:\d+(?:\.\d*)?|\.\d+)(?:e[+-]?\d+)?)/i,a,"0123456789"],["pun",/^.[^\s\w$'./@]*/,a]]),["pascal"]);
