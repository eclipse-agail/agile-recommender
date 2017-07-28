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
PR.registerLangHandler(PR.createSimpleLexer([["pln",/^[\t\n\r \xa0]+/,null,"\t\n\r \u00a0"],["str",/^(?:"(?:[^"\\]|\\[\S\s])*(?:"|$)|'(?:[^'\\]|\\[\S\s])*(?:'|$))/,null,"\"'"]],[["com",/^--(?:\[(=*)\[[\S\s]*?(?:]\1]|$)|[^\n\r]*)/],["str",/^\[(=*)\[[\S\s]*?(?:]\1]|$)/],["kwd",/^(?:and|break|do|else|elseif|end|false|for|function|if|in|local|nil|not|or|repeat|return|then|true|until|while)\b/,null],["lit",/^[+-]?(?:0x[\da-f]+|(?:\.\d+|\d+(?:\.\d*)?)(?:e[+-]?\d+)?)/i],
["pln",/^[_a-z]\w*/i],["pun",/^[^\w\t\n\r \xa0][^\w\t\n\r "'+=\xa0-]*/]]),["lua"]);
