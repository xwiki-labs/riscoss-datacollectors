/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
var JSDom = require("jsdom");

// var s = document.createElement('script'); s.src="http://code.jquery.com/jquery.js"; document.body.appendChild(s);

var getCount = function (name, $) {
    var k = $('td:contains(' + JSON.stringify(name) + ')');
    var x = k[k.length-1];
    return x.previousSibling.textContent;
};

var makeReq = function (url, callback) {
    var out = [];
    JSDom.env(url, ["http://code.jquery.com/jquery.js"], function (errors, window) {
        var $ = window.$;
        out.push({ type:'NUMBER', id:'Unique_Lic', value: getCount("Unique licenses", $) });
        out.push({ type:'NUMBER', id:'Number_Lic', value: getCount("Licenses found", $) });
        out.push({ type:'NUMBER', id:'Files_No_Lic', value: getCount("Files with no licenses", $) });
        out.push({ type:'NUMBER', id:'Files_No_Lic', value: getCount("Files", $) });
        callback(out);
    });
};

var run = function (conf) {
    makeReq(conf.fossologyURL, function (data) {
        console.log('-----BEGIN RISK DATA-----');
        console.log(JSON.stringify(data));
        console.log('-----END RISK DATA-----');
    });
};

var main = function () {
    process.stdin.resume();
    process.stdin.setEncoding('utf8');
    var data = "";
    process.stdin.on('data', function(chunk) { data += chunk; });
    process.stdin.on('end', function() { run(JSON.parse(data)); });
};
main();
