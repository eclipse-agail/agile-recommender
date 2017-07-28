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
PR.registerLangHandler(PR.createSimpleLexer([["opn",/^{+/,a,"{"],["clo",/^}+/,a,"}"],["com",/^#[^\n\r]*/,a,"#"],["pln",/^[\t\n\r \xa0]+/,a,"\t\n\r \u00a0"],["str",/^"(?:[^"\\]|\\[\S\s])*(?:"|$)/,a,'"']],[["kwd",/^(?:after|append|apply|array|break|case|catch|continue|error|eval|exec|exit|expr|for|foreach|if|incr|info|proc|return|set|switch|trace|uplevel|upvar|while)\b/,a],["lit",/^[+-]?(?:[#0]x[\da-f]+|\d+\/\d+|(?:\.\d+|\d+(?:\.\d*)?)(?:[de][+-]?\d+)?)/i],["lit",
/^'(?:-*(?:\w|\\[!-~])(?:[\w-]*|\\[!-~])[!=?]?)?/],["pln",/^-*(?:[_a-z]|\\[!-~])(?:[\w-]*|\\[!-~])[!=?]?/i],["pun",/^[^\w\t\n\r "'-);\\\xa0]+/]]),["tcl"]);
