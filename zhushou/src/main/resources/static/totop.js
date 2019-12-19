/**
 * Created on : 2018-01-11 18:31:19 星期四
 * Encoding   : UTF-8
 * Description: 答题小助手，哈哈哈哈哈哈
 *
 * @author    @qii404 <qii404.me>
 * @copyright qii404.me.
 */

var qii404 = {

    /**
     * 题目url
     */
    // questionUrl: 'http://wd.sa.sogou.com/api/ans?key=',
    // questionUrl: 'http://140.143.49.31/api/ans2?wdcallback=xx&key=',
    questionUrl: 'http://localhost:8234/getResult',
    
    questiontemp:'',
    /**
     * 搜索链接
     */
    searchUrl: 'http://www.baidu.com/s?wd=',

    /**
     * 分析结果
     */
    analysisResults: {},

    /**
     * 轮询时间
     */
    interval: 500,


    /**
     * 定时器
     */
    timer: null,

    /**
     * 计数器
     */
    counter: 0,

    /**
     * 当前应用
     */
    preApp: 'cddh',

    base64Handler: null,

    /**
     * 当前题目
     */
    question: '',

    /**
     * 选项
     */
    answers: [],

    /**
     * 初始化
     */
    init: function() {
        this.base64Handler = new Base64();
        this.bindChangeApp();
        // this.bindButton();
        this.runTimer();
    },

    /**
     * 绑定app切换
     */
    bindChangeApp: function() {
        var this_ = this;

        $('#app-type').change(function(e) {

            var val = $(this).children('option:selected').val();

            if (!val) {
                clearInterval(this_.timer)
                return;
            }

            this_.preApp = val;

            clearInterval(this_.timer);
            this_.runTimer();
        });
    },

    /**
     * 启动计时器
     */
    runTimer: function() {
        this.getQuestion();
        var this_ = this;

        this.timer = setInterval(function() {
            this_.getQuestion();
        }, this.interval);
    },

    /**
     * 按钮绑定
     */
    bindButton: function() {

        var this_ = this;

        $('#stop').on('click', function() {
            clearInterval(this_.timer);
            this_.renderCounter('stop');
        });

        $('#start').on('click', function() {
            clearInterval(this_.timer);
            this_.runTimer();
        });
    },

    /**
     * 获取问题 && 后续流程
     */
    getQuestion: function() {

       this.renderCounter();
        var this_ = this;

    /*  var settings = {
        type: "GET",
        url:this.getQuestionUrl()+'',
        dataType:"json",
        error: function(XHR,textStatus,errorThrown) {
           // alert ("XHR="+XHR+"\ntextStatus="+textStatus+"\nerrorThrown=" + errorThrown);
           console.log("textStatus:" + textStatus);
        },
        success: function(data,textStatus) {
            data = JSON.parse(data.slice(3,-1));
            data.result = JSON.parse(this_.base64Handler.decode(data.result));
            data = JSON.parse(data.result[data.result.length - 1]);
            if (!(data.title && data.title != this_.question)) {
                return;
            }

            data = this_.formatData(data);

            this_.question = data.title;
            this_.answers = data.answers;

            this_.renderPage(data);
            this_.analysisQuestion();
        },
        headers: {
         /*"Accept":"",
         "Accept-Encoding": "gzip, deflate",
         "Accept-Language": "zh-CN,en-US;q=0.8",
         "User-Agent":"Mozilla/5.0 (Linux; Android 6.0; CAM-TL00H Build/HONORCAM-TL00H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36 SGInfo/720/1208/2.0 SogouSearch Android1.0 version3.0 AppVersion/7351",
         "X-Requested-With": "com.sogou.activity.src",
         "Cookie": "APP-SGS-ID=ab26860863030234538",
         "Host":"wzpush.sogoucdn.com",
         "Referer":"https://wzassistant.sogoucdn.com/v5/cheat-sheet?channel=bwyx&icon=http%3A%2F%2Fapp.sastatic.sogoucdn.com%2Fpic%2Fthyx2.png&name=%E5%A4%B4%E5%8F%B7%E8%8B%B1%E9%9B%84&appName=undefined",
         "Connection":"keep-alive",
         "Access-Control-Allow-Headers":"X-Requested-With"
        }
    };
    $.ajax(settings);*/



/*
     var ajax = function(url){
        var xhr = new XMLHttpRequest(),
        result;
        xhr.open('get', url, true);
        xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
         
        xhr.setRequestHeader("Accept-Encoding", "gzip, deflate");
        xhr.setRequestHeader("Accept-Language", "zh-CN,en-US;q=0.8");
        xhr.setRequestHeader("User-Agent","Mozilla/5.0 (Linux; Android 6.0; CAM-TL00H Build/HONORCAM-TL00H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36 SGInfo/720/1208/2.0 SogouSearch Android1.0 version3.0 AppVersion/7351");
        xhr.setRequestHeader("X-Requested-With", "com.sogou.activity.src");
        xhr.setRequestHeader("Cookie", "APP-SGS-ID=ab26860863030234538");//自己登录的cookie信息
        xhr.setRequestHeader("Host", "wzpush.sogoucdn.com");
        xhr.setRequestHeader("Referer","https://wzassistant.sogoucdn.com/v5/cheat-sheet?channel=bwyx&icon=http%3A%2F%2Fapp.sastatic.sogoucdn.com%2Fpic%2Fthyx2.png&name=%E5%A4%B4%E5%8F%B7%E8%8B%B1%E9%9B%84&appName=undefined");
        xhr.setRequestHeader("Connection", "keep-alive");
        //xhr.send(post_data);
        xhr.onreadystatechange=function(){
            if (xhr.readyState==4 && xhr.status==200){
                result = JSON.parse(xhr.responseText);
                console.log("result:"+result);
                
            }else if(xhr.readyState==4 && (!xhr.status==200)){
                console.log('Ajax Return Error!');
            }
        }
    }
    ajax(this.getQuestionUrl());
*/

       
        $.get(this.getQuestionUrl(), function(data) {
            /*console.log("data:" + data);
            data = JSON.parse(data.slice(3,-1));
            data.result = JSON.parse(this_.base64Handler.decode(data.result));
            data = JSON.parse(data.result[data.result.length - 1]);
            if (!(data.title && data.title != this_.question)) {
                return;
            }*/
           
            //console.log("data: " + data.question);
             //data = JSON.parse(data);
            //data = this_.formatData(data);
            if(data.question == this_.questiontemp){
              return;
            }
            this_.questiontemp = data.question;
            for(var i = 0;i < data.results.length;i++){
               $('#result' + i).html(data.results[i]);
            }
            this_.question = data.question;
            $('#question0').html(data.question);
            console.log("data.answers:" + data.answers[0]);
            this_.answers = data.answers;

            this_.renderPage(data);
            this_.analysisQuestion();
        });
    },

    /**
     * 数据格式处理
     */
    formatData: function(data) {
        for (var i = 0; i < data.answers.length; i++) {
            data.answers[i] = {index: i, value: data.answers[i].trim()}
        }

        return data;
    },

    /**
     * 渲染页面
     */
    renderPage: function(data) {
        var template = $('#question-template').html();

        Mustache.parse(template);
        $('#question-container').html(Mustache.render(template, data));

        this.analysisSingleSearch();
        this.renderSearchPage(data.title);
    },

    /**
     * 获取题目url
     */
    getQuestionUrl: function() {
        //return this.questionUrl + this.preApp;
         return this.questionUrl;
    },

    /**
     * 渲染计数器
     */
    renderCounter: function(type) {
        $('#status').html(type == undefined ? '刷新中': '已停止');
        $('#counter').html(type == undefined ? ++this.counter : this.counter);
    },

    /**
     * 分析单页搜索结果
     */
    analysisSingleSearch: function() {
        var this_ = this;
        function B(html){
            //console.log("html--------" + html);
            html = this_.trimSymble(this_.resolveHtml(html));

            for (var i = 0; i < this_.answers.length; i++) {
            	//this_.answers[i].value = this_.answers[i].value.substring(this_.answers[i].value.indexOf(',') + 1);
            	//console.log(" ____________ " + this_.answers[i]);
                var matches = html.match(new RegExp(this_.trimSymble(this_.answers[i]), 'ig'));
                var num = matches ? matches.length : 0;

                $('#search-score-' + i).html(num);
            }

            var removedRepeat = this_.removeRepeat();
            if (removedRepeat) {
                $('.removed-repeat').show();
                for (var i = 0; i < removedRepeat.length; i++) {
                    var matches = html.match(new RegExp(this_.trimSymble(removedRepeat[i]), 'ig'));
                    var num = matches ? matches.length : 0;

                    $('#removed-score-' + i).html(removedRepeat[i] + ' ' + num);
                }
            }
        }
        $.get(this.getSearchUrl(this.question), function(html) {
              B(html);
              var str = ' ';
              for(var i = 0; i < this_.answers.length; i++){
                  str += this_.answers[i] + ' ';

              }
              if(this_.question)
              str += this_.question.substring(this_.question.indexOf('.') + 1);
              $.get("http://www.baidu.com/s?wd=" + str, function(html) {
                    html = this_.trimSymble(this_.resolveHtml(html));

		            for (var i = 0; i < this_.answers.length; i++) {
		                var matches = html.match(new RegExp(this_.trimSymble(this_.answers[i]), 'ig'));
		                var num = matches ? matches.length : 0;

		                $("#an-sq-" + i).html(num);
		            }
              });
        });
    },

    /**
     * 去除选项中的重复内容
     */
    removeRepeat: function() {
      /**  var diff = false;
        var len  = this.answers[0].value.length;
        var arr  = [this.answers[0].value.split('')];

        for (var i = 1; i < this.answers.length; i++) {
            if (this.answers[i].value.length != len) {
                diff = true;
                break;
            }
            arr.push(this.answers[i].value.split(''));
        }

        if (!diff) {
            var indexes = [];
            for (var i = 0; i < len; i++) {
                if (
                    arr[0][i] == arr[1][i] &&
                    arr[1][i] == arr[2][i]
                ) {
                    indexes.push(i);
                }
            };

            if (indexes == '') {
                return false;
            }

            var count = 0;
            for (var k = 0; k < indexes.length; k++) {
                arr[0].splice(indexes[k] - count, 1);
                arr[1].splice(indexes[k] - count, 1);
                arr[2].splice(indexes[k] - count, 1);
                count++;
            };

            arr[0] = arr[0].join('');
            arr[1] = arr[1].join('');
            arr[2] = arr[2].join('');

            console.log(arr);

            return arr;
        }
*/
        return false;
    },

    /**
     * 处理html文本
     */
    resolveHtml: function(html) {
        html = html.replace(/<script[\s\S]*?<\/script>/ig, '');
        html = html.replace(/<style[\s\S]*?<\/style>/ig, '');

        return html.replace(/<\/?[\s\S]+?>/g, '');
    },

    /**
     * 去除标点符号
     */
    trimSymble: function(string) {
        return string.replace(/\s|，|,|。|\.|·|《|》|<|>|-|=/g, '');
    },

    /**
     * 渲染下部搜索页面
     */
    renderSearchPage: function(question) {

        $('#iframe-page').attr('src', this.getSearchUrl(this.answers[0]));
        $('#iframe-page2').attr('src', this.getSearchUrl(this.answers[1]));
        $('#iframe-page3').attr('src', this.getSearchUrl(this.answers[2]));
        return;

        var this_ = this;

        $.get(this.getSearchUrl(question), function(html) {
            html = html.replace('//www.baidu.com/img/baidu_jgylogo3.gif', 'http://www.baidu.com/img/baidu_jgylogo3.gif');
            html = html.replace(this_.getAnswersPreg(), '<span class="highlight-answer">$1</span>');
            $('#div-page').html(html);
        });
    },

    /**
     * 获取答案正则
     */
    getAnswersPreg: function() {
        var preg = [];

        for (var i = 0; i < this.answers.length; i++) {
            preg.push(this.answers[i]);
        }

        return new RegExp('(' + preg.join('|') + ')', 'ig');
    },

    /**
     * 分析题干
     */
    analysisQuestion: function() {
        /*var this_ = this;

        for (var i = 0; i < this.answers.length; i++) {
            var url = this.getSearchUrl(this.question, this.answers[i].value);

            ~(function(url, j) {
                $.get(url, function(html) {
                    var match = html.match(/百度为您找到相关结果约([\d|,]+)个/);
                    $('#total-score-' + j).html(match[1] ? match[1] : 0);

                    url = j = null;
                });
            })(url, i);
        }
        */
    },

    /**
     * 进行搜索
     */
    search: function(question) {
        var url = this.getSearchUrl(question);
        var this_ = this;

        $.get(url, function(html) {
            this_.analysisHtml(html);
            this_.renderAnalysis();
        });
    },

    /**
     * 获取搜索url
     *
     */
    getSearchUrl: function(question, answer) {
        //var suffix = answer ? (' ' + answer) : '';
        //console.log("***********************");
        //console.log( this.searchUrl + question.substring(question.indexOf('.') + 1));
        
        //return this.searchUrl + question.substring(question.indexOf('.') + 1);

        //return this.searchUrl + question.substring(question.indexOf('.') + 1) + suffix;
        //if(question.indexOf('.') == -1)
        	//return this.searchUrl + question + suffix;
    	return this.searchUrl+question;
    },

    /**
     * 分析html
     *
     */
    analysisHtml: function(html) {
        var this_ = this;
        this.answers.forEach(function(item) {
            var re = eval('/' + item + '/ig');
            var matches = html.match(re);
            this_.analysisResults[item] = matches ? matches.length : 0;
        });

        console.log(this.analysisResults);
    },

    /**
     * 渲染分析结果
     */
    renderAnalysis: function() {
        var options = '';
        var index = 0;

        for (var i in this.analysisResults) {
            options += this.choice[index] + '、' + i + ': <span class="label label-default">' + this.analysisResults[i] + '</span><br>';
            index++;
        }

        $('#result').html(options);
    }
}

qii404.init();
