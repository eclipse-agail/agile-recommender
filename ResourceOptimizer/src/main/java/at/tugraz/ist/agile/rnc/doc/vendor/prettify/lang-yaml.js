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
PR.registerLangHandler(PR.createSimpleLexer([["pun",/^[:>?|]+/,a,":|>?"],["dec",/^%(?:YAML|TAG)[^\n\r#]+/,a,"%"],["typ",/^&\S+/,a,"&"],["typ",/^!\S*/,a,"!"],["str",/^"(?:[^"\\]|\\.)*(?:"|$)/,a,'"'],["str",/^'(?:[^']|'')*(?:'|$)/,a,"'"],["com",/^#[^\n\r]*/,a,"#"],["pln",/^\s+/,a," \t\r\n"]],[["dec",/^(?:---|\.\.\.)(?:[\n\r]|$)/],["pun",/^-/],["kwd",/^\w+:[\n\r ]/],["pln",/^\w+/]]),["yaml","yml"]);
