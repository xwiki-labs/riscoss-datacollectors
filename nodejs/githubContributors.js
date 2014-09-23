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
var https = require('https');
var http = require('http');

// This must match the name of the RISCOSSPlatformCode.DataCollector object
var COLLECTOR_ID = "GithubContributors";

// One of [ NUMBER, EVIDENCE, DISTRIBUTION ]
var COLLECTOR_DATATYPE = "NUMBER";

var makeReq = function (project, callback) {
    https.request({
        host: 'api.github.com',
        path: '/repos/' + project + '/collaborators',
        headers: {
            'User-Agent': 'riscoss.eu - OSS Data Collector'
        }
    }, function(response) {
      var str = '';
      response.on('data', function (chunk) { str += chunk; });
      response.on('end', function () {
          var obj = JSON.parse(str);
          callback(obj.length);
      });
    }).end();
};

var run = function (conf) {
    makeReq(conf.githubProject, function (count) {
        var req = http.request({
            method: 'POST',
            host: conf.riscoss_rdrHost,
            port: conf.riscoss_rdrPort,
            path: conf.riscoss_rdrPath,
        }, function (response) {
            console.log('ok');
        });
        req.end(JSON.stringify([
            {
                id: COLLECTOR_ID,
                type: COLLECTOR_DATATYPE,
                target: conf.riscoss_targetName,
                value: count
            }
        ]));
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
