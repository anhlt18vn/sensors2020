/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rdf4led.query.sparql;

/**
 * Tags.java
 *
 * <p>TODO Name Tags to
 *
 * <p>Author : Anh Le-Tuan Contact: anh.le@deri.org anh.letuan@insight-centre.org
 *
 * <p>10 Sep 2015
 */
public class Tags {
  public static final String LPAREN = "(";
  public static final String RPAREN = ")";

  public static final String LBRACKET = "[";
  public static final String RBRACKET = "]";

  public static final String LBRACE = "{";
  public static final String RBRACE = "}";

  // -- Common terms
  //    public static final byte tagUndef         = 1;//"undef" ;
  public static final byte tagNull = 2; // "null" ;
  //    public static final byte tagDefault       = 3;//"default" ;
  //    public static final byte tagExec          = 4;//"exec" ;
  //    public static final byte tagRow           = 5;//"row" ;
  //    public static final byte tagVars          = 6;//"vars" ;
  //
  //    // RDF
  public static final byte tagGraph = 7; // "graph" ;
  //    public static final byte tagLoad          = 8;//"graph@" ;
  public static final byte tagTriple = 9; // "triple" ;
  //    public static final byte tagQuad          = 10;//"quad" ;
  //    public static final byte tagTriplePath    = 11;//"path" ;
  //
  //    public static final byte tagBase          = 12;//"base" ;
  //    public static final byte tagPrefix        = 13;//"prefix" ;
  //    public static final byte tagPrefixMap     = 14;//"prefixmap" ;
  //    public static final byte tagPrefixMapping = 15;//"prefixmapping" ;
  //
  //    // SPARQL
  //    public static final byte tagDataset       = 16; //"dataset" ;
  //    public static final byte tagBinding       = 17; //"binding" ;
  public static final byte tagTable = 18; // "table" ;
  //    public static final byte tagResultSet     = 19;//"resultset" ;
  //
  //    // SPARQL algebra
  public static final byte tagBGP = 20; // "bgp" ;
  public static final byte tagQuadPattern = 21; // "quadpattern" ;
  public static final byte tagFilter = 22; // "filter" ;
  //    // public static final String tagGraph = "graph" ;
  public static final byte tagLabel = 23; // "label" ;
  public static final byte tagService = 24; // "service" ;
  public static final byte tagProc = 25; // "proc" ;
  public static final byte tagPropFunc = 26; // "propfunc" ;
  public static final byte tagJoin = 27; // "join" ;
  public static final byte tagMutilJoin = 27; // "join" ;
  public static final byte tagSequence = 28; // "sequence" ;
  public static final byte tagDisjunction = 29; // "disjunction" ;
  public static final byte tagLeftJoin = 30; // "leftjoin" ;
  public static final byte tagConditional = 31; // "conditional" ;
  public static final byte tagDiff = 32; // "diff" ;
  public static final byte tagMinus = 33; // "minus" ;
  public static final byte tagUnion = 34; // "union" ;
  //    public static final byte tagDatasetNames  = 35;//"datasetnames" ;
  //
  public static final byte tagToList = 36; // "tolist" ;
  public static final byte tagOrderBy = 37; // "order" ;
  public static final byte tagTopN = 38; // "top" ;
  public static final byte tagGroupBy = 39; // "group" ;
  public static final byte tagProject = 40; // "project" ;
  public static final byte tagDistinct = 41; // "distinct" ;
  public static final byte tagReduced = 42; // "reduced" ;
  public static final byte tagAssign = 43; // "assign" ;
  public static final byte tagExtend = 44; // "extend" ;
  //    public static final byte symAssign        = 45;//":=" ;
  public static final byte tagSlice = 46; // "slice" ;
  public static final byte tagExt = 47; // "tagExt" ;
  //
  //    // Paths
  public static final byte tagPath = 48; // "path" ;
  //    public static final byte tagPathSeq       = 49; //"seq" ;
  //    public static final byte tagPathAlt       = 49;//"alt" ;
  //    public static final byte tagPathMod       = 49;//"mod" ;
  //
  //    public static final byte tagPathZeroOrMore1   = 49;//"path*" ;
  //    public static final byte tagPathZeroOrMoreN   = 50;//"pathN*" ;
  //    public static final byte tagPathOneOrMore1    = 51;//"path+" ;
  //    public static final byte tagPathOneOrMoreN    = 52;//"pathN+" ;
  //    public static final byte tagPathZeroOrOne     = 53;//"path?" ;
  //    public static final byte tagPathFixedLength   = 54;//"pathN" ;
  //    public static final byte tagPathDistinct      = 55;//"distinct" ;
  //    public static final byte tagPathMulti         = 56;//"multi" ;
  //    public static final byte tagPathShortest      = 57;//"shortest" ;
  //
  //    public static final byte tagPathReverse       = 58;//"reverse" ;
  //    public static final byte tagPathRev           = 59;//"rev" ;
  //    public static final byte tagPathLink          = 60;//"link" ;
  //    public static final byte tagPathNotOneOf      = 61;//"notoneof" ;
  //
  //    // Not used - nowadays extensions are not explicitly flagged in the algebra.
  //    // But needed to override existing operations.
  //
  //    // Expressions
  //    // sym => swiggly thing, tag => word-ish thing
  //
  //    public static final byte tagExpr          = 62;//"org.rdf4led.common.data.expr" ;
  //    public static final byte tagExprList      = 63;//"exprlist" ;
  //
  public static final byte tagEQ = 64; // "=" ;
  public static final byte tagNE = 65; // "!=" ;
  public static final byte tagGT = 66; // ">" ;
  public static final byte tagLT = 67; // "<" ;
  public static final byte tagLE = 68; // "<=" ;
  public static final byte tagGE = 69; // ">=" ;
  //    public static final byte symOr            = 70;//"||" ;
  public static final byte tagOr = 71; // "or" ;
  //    public static final byte symAnd           = 72;//"&&" ;
  public static final byte tagAnd = 73; // "and" ;
  public static final byte tagAdd = 74; // "+" ;
  public static final byte tagSub = 75; // "-" ;
  public static final byte tagMult = 76; // "*" ;
  public static final byte tagDiv = 77; // "/" ;
  public static final byte tagNot = 78; // "!" ;
  //
  //    public static final byte tagNot           = 79;//"not" ;
  public static final byte tagStr = 80; // "str" ;
  public static final byte tagStrLang = 81; // "strlang" ;
  public static final byte tagStrDatatype = 82; // "strdt" ;
  public static final byte tagRand = 83; // "rand" ;
  //
  public static final byte tagLang = 84; // "org.rdf4led.rdf.org.rdf4led.sparql.parser.lang" ;
  public static final byte tagLangMatches = 85; // "langmatches" ;
  public static final byte tagSameTerm = 86; // "sameterm" ;

  public static final byte tagDatatype = 87; // "datatype" ;
  public static final byte tagBound = 88; // "bound" ;
  public static final byte tagCoalesce = 89; // "coalesce" ;
  public static final byte tagIf = 90; // "if" ;

  public static final byte tagIsNumeric = 90; // "if" ;
  public static final byte tagIsIRI = 91; // "isIRI" ;
  public static final byte tagIsURI = 92; // "isURI" ;
  public static final byte tagIsBlank = 93; // "isBlank" ;
  public static final byte tagIsLiteral = 94; // "isLiteral" ;
  public static final byte tagRegex = 95; // "regex" ;
  public static final byte tagExists = 96; // "exists" ;
  public static final byte tagNotExists = 97; // "notexists" ;
  //

  public static final byte tagYear = 98; // "year" ;
  public static final byte tagMonth = 99; // "month" ;
  public static final byte tagDay = 100; // "day" ;
  public static final byte tagHours = 101; // "hours" ;
  public static final byte tagMinutes = 102; // "minutes" ;
  public static final byte tagSeconds = 103; // "seconds" ;
  public static final byte tagTimezone = 104; // "timezone" ;
  public static final byte tagTZ = 105; // "tz" ;
  public static final byte tagNow = 106; // "now" ;

  public static final byte tagUUID = 107; // "uuid" ;
  public static final byte tagStrUUID = 108; // "struuid" ;
  public static final byte tagVersion = 109; // "version" ;
  //
  public static final byte tagMD5 = 110; // "md5" ;
  public static final byte tagSHA1 = 111; // "sha1" ;
  public static final byte tagSHA224 = 112; // "sha224" ;
  public static final byte tagSHA256 = 113; // "sha256" ;
  public static final byte tagSHA384 = 114; // "sha384" ;
  public static final byte tagSHA512 = 115; // "sha512" ;
  //
  public static final byte tagStrlen = 116; // "strlen" ;
  public static final byte tagSubstr = 117; // "substr" ;
  public static final byte tagReplace = 118; // "replace" ;
  public static final byte tagStrUppercase = 119; // "ucase" ;
  public static final byte tagStrLowercase = 120; // "lcase" ;
  public static final byte tagStrEnds = 121; // "strends" ;
  public static final byte tagStrStarts = 122; // "strstarts" ;
  public static final byte tagStrBefore = 123; // "strbefore" ;
  public static final byte tagStrAfter = 124; // "strafter" ;
  public static final byte tagStrContains = 125; // "contains" ;
  public static final byte tagStrEncodeForURI = 126; // "encode_for_uri" ;
  public static final byte tagConcat = 127; // "concat" ;

  public static final byte tagNumAbs = 12; // "abs" ;
  public static final byte tagNumRound = 110; // "round" ;
  public static final byte tagNumCeiling = 49; // "ceil" ;
  public static final byte tagNumFloor = 49; // "floor" ;
  //
  public static final byte tagBNode = 49; // "bnode" ;
  //    public static final byte tagIri           = 49;//"iri" ;
  public static final byte tagUri = 49; // "uri" ;
  //
  public static final byte tagIn = 49; // "in" ;
  public static final byte tagNotIn = 49; // "notin" ;
  public static final byte tagCall = 49; // "call" ;

  public static final byte tagUnaryPlus = 49; // "call" ;
  public static final byte tagUnaryMinus = 49;

  //    public static final byte tagTrue          = 49;//"true" ;
  //    public static final byte tagFalse         = 49;//"false" ;
  //
  //    public static final byte tagAsc           = 49;//"asc" ;
  //    public static final byte tagDesc          = 49;//"desc" ;
  //
  //    public static final byte tagCount         = 49;//"count" ;
  //    public static final byte tagSum           = 49;//"sum" ;
  //    public static final byte tagMin           = 49;//"min" ;
  //    public static final byte tagMax           = 49;//"max" ;
  //    public static final byte tagAvg           = 49;//"avg" ;
  //    public static final byte tagSample        = 49;//"sample" ;
  //    public static final byte tagGroupConcat   = 49;//"group_concat" ;
  //    public static final byte tagSeparator     = 49;//"separator" ;
}
