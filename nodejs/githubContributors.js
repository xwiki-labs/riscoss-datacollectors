/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
var https = require('https');

var COLLECTOR_ID = 'GithubContributors';
var DATA_TYPE = 'NUMBER';

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
        console.log('-----BEGIN RISK DATA-----');
        console.log(JSON.stringify([{value:count, type:DATA_TYPE, id:COLLECTOR_ID}]));
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
