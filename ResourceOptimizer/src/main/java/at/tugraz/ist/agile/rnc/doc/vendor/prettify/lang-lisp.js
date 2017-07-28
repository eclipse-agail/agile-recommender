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
PR.registerLangHandler(PR.createSimpleLexer([["opn",/^\(+/,a,"("],["clo",/^\)+/,a,")"],["com",/^;[^\n\r]*/,a,";"],["pln",/^[\t\n\r \xa0]+/,a,"\t\n\r \u00a0"],["str",/^"(?:[^"\\]|\\[\S\s])*(?:"|$)/,a,'"']],[["kwd",/^(?:block|c[ad]+r|catch|con[ds]|def(?:ine|un)|do|eq|eql|equal|equalp|eval-when|flet|format|go|if|labels|lambda|let|load-time-value|locally|macrolet|multiple-value-call|nil|progn|progv|quote|require|return-from|setq|symbol-macrolet|t|tagbody|the|throw|unwind)\b/,a],
["lit",/^[+-]?(?:[#0]x[\da-f]+|\d+\/\d+|(?:\.\d+|\d+(?:\.\d*)?)(?:[de][+-]?\d+)?)/i],["lit",/^'(?:-*(?:\w|\\[!-~])(?:[\w-]*|\\[!-~])[!=?]?)?/],["pln",/^-*(?:[_a-z]|\\[!-~])(?:[\w-]*|\\[!-~])[!=?]?/i],["pun",/^[^\w\t\n\r "'-);\\\xa0]+/]]),["cl","el","lisp","lsp","scm","ss","rkt"]);
