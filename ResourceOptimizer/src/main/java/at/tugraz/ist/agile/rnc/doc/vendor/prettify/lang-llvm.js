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
PR.registerLangHandler(PR.createSimpleLexer([["pln",/^[\t\n\r \xa0]+/,null,"\t\n\r \u00a0"],["str",/^!?"(?:[^"\\]|\\[\S\s])*(?:"|$)/,null,'"'],["com",/^;[^\n\r]*/,null,";"]],[["pln",/^[!%@](?:[$\-.A-Z_a-z][\w$\-.]*|\d+)/],["kwd",/^[^\W\d]\w*/,null],["lit",/^\d+\.\d+/],["lit",/^(?:\d+|0[Xx][\dA-Fa-f]+)/],["pun",/^[(-*,:<->[\]{}]|\.\.\.$/]]),["llvm","ll"]);
