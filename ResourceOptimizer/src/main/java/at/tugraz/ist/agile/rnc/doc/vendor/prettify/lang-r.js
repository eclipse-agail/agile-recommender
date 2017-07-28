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
PR.registerLangHandler(PR.createSimpleLexer([["pln",/^[\t\n\r \xa0]+/,null,"\t\n\r \u00a0"],["str",/^"(?:[^"\\]|\\[\S\s])*(?:"|$)/,null,'"'],["str",/^'(?:[^'\\]|\\[\S\s])*(?:'|$)/,null,"'"]],[["com",/^#.*/],["kwd",/^(?:if|else|for|while|repeat|in|next|break|return|switch|function)(?![\w.])/],["lit",/^0[Xx][\dA-Fa-f]+([Pp]\d+)?[Li]?/],["lit",/^[+-]?(\d+(\.\d+)?|\.\d+)([Ee][+-]?\d+)?[Li]?/],["lit",/^(?:NULL|NA(?:_(?:integer|real|complex|character)_)?|Inf|TRUE|FALSE|NaN|\.\.(?:\.|\d+))(?![\w.])/],
["pun",/^(?:<<?-|->>?|-|==|<=|>=|<|>|&&?|!=|\|\|?|[!*+/^]|%.*?%|[$=@~]|:{1,3}|[(),;?[\]{}])/],["pln",/^(?:[A-Za-z]+[\w.]*|\.[^\W\d][\w.]*)(?![\w.])/],["str",/^`.+`/]]),["r","s","R","S","Splus"]);
