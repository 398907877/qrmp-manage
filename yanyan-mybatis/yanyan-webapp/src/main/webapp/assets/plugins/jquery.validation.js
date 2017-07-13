(function() {
  var ValidateUtil = function() {
    // value是否空
    this.isEmpty = function(value) {
      if (value) {
        return false;
      } else {
        return true;
      }
    };
    // value是否空白串
    this.isBlank = function(value) {
      if(!value) return true;
      return this.isLegalValue(value, new RegExp("^\\s*$"));
    };
    // value是否电话号码
    this.isPhone = function(value) {
      return this.isLegalValue(value, new RegExp("(^1[3|5|8]{1}[0-9]{9}$)|(^0[0-9]{10,11}$)|(^[1-9]{1}[0-9]{6,7}$)"));
    };
    // value是否移动电话号码
    this.isMobile = function(value) {
      return this.isLegalValue(value, new RegExp("(^1[3|5|8]{1}[0-9]{9}$)"));
    };
    // value是否email
    this.isEmail = function(value) {
      return this.isLegalValue(value, new RegExp("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$"));
    };
    // value是否URL串
    this.isURL = function(value) {
      return this.isLegalValue(value, /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/);
    };
    // value是否IP
    this.isIP = function(value) {
      return this.isLegalValue(value, /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/);
    };
    // value是否货币金额
    this.isCurrency = function(value) {
      return this.isLegalValue(value, /^\d+(\.\d+)?$/);
    };
    // value是否邮政编码
    this.isPostcode = function(value) {
      return this.isLegalValue(value, /^[1-9]\d{5}$/);
    };
    // value是否密码(字母加数字加特殊符)
    this.isPassword = function(value) {
      return this.isLegalValue(value, /^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/);
    };
    // value是否有效文件,filter用逗号隔开
    this.isValidFile = function(value, filter) {
      return this.isLegalValue(value, new RegExp("^.+\.(?=EXT)(EXT)$".replace(/EXT/g, filter.split(/\s*,\s*/).join("|")), "gi"));
    };
    // value是否身份证号
    this.isIdCard = function(value) {
      var number = value;
      var date, Ai;
      var verify = "10x98765432";
      var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
      var area = [ '', '', '', '', '', '', '', '', '', '', '', '北京', '天津', '河北', '山西', '内蒙古', '', '', '', '', '', '辽宁', '吉林', '黑龙江', '', '', '', '', '', '', '', '上海', '江苏', '浙江', '安微', '福建', '江西', '山东', '', '', '', '河南', '湖北', '湖南', '广东', '广西', '海南', '', '', '', '重庆', '四川', '贵州', '云南', '西藏', '', '', '', '', '', '', '陕西', '甘肃', '青海', '宁夏', '新疆', '', '', '', '', '', '台湾', '', '', '', '', '', '', '', '', '', '香港', '澳门', '', '', '', '', '', '', '', '', '国外' ];
      var re = number.match(/^(\d{2})\d{4}(((\d{2})(\d{2})(\d{2})(\d{3}))|((\d{4})(\d{2})(\d{2})(\d{3}[x\d])))$/i);
      if (re == null)
        return false;
      if (re[1] >= area.length || area[re[1]] == "")
        return false;
      if (re[2].length == 12) {
        Ai = number.substr(0, 17);
        // date = [ re[9], re[10], re[11] ].join("-");
        if (!(this.isLegalValue(re[9], /^\d{4}$/) && this.isLegalValue(re[10], /^\d{2}$/) && this.isLegalValue(re[11], /^\d{2}$/))) {
          return false;
        }
        var mm = parseInt(re[10]);
        if (mm < 1 && mm > 12) {
          return false;
        }
        var dd = parseInt(re[11]);
        if (dd < 1 && dd > 31) {
          return false;
        }
      } else {
        Ai = number.substr(0, 6) + "19" + number.substr(6);
        // date = [ "19" + re[4], re[5], re[6] ].join("-");
        if (!(this.isLegalValue(re[4], /\d{2}/) && this.isLegalValue(re[5], /\d{2}/) && this.isLegalValue(re[6], /\d{2}/))) {
          return false;
        }
        var mm = parseInt(re[5]);
        if (mm < 1 && mm > 12) {
          return false;
        }
        var dd = parseInt(re[6]);
        if (dd < 1 && dd > 31) {
          return false;
        }
      }
      /*if (!this.IsDate(date, "ymd"))
        return false;*/
      var sum = 0;
      for ( var i = 0; i <= 16; i++) {
        sum += Ai.charAt(i) * Wi[i];
      }
      Ai += verify.charAt(sum % 11);

      if (!(number.length == 15 || number.length == 18 && number == Ai)) {
        return false;
      } else {
        return true;
      }
    };
    // value是否字母
    this.isEnglish = function(value) {
      return this.isLegalValue(value, /^[A-Za-z]+$/);
    };
    // value是否汉字
    this.isChinese = function(value) {
      return this.isLegalValue(value, /^[\u0391-\uFFE5]+$/);
    };
    // value是否用户名(英文字母或英文字母加数字)
    this.isUsername = function(value) {
      return this.isLegalValue(value, /^[a-z]\w{3,}$/i);
    };
    // value是否符合规则，reg为RegExp对象
    this.isLegalValue = function(value, reg) {
      if (value != null && value != "") {        
        if (!reg.test(value)) {
          return false;
        } else {
          return true;
        }
      } else {
        return false;
      }
    };
    // **********************************数字***************************
    // value是否整数
    this.isInteger = function(value) {
      return this.isLegalValue(value, /^[-\+]?\d+$/);
    };
    // value是否正整数
    this.isPositiveInteger = function(value) {
      return this.isLegalValue(value, /^[\+]?\d+$/);
    };
    // value是否负整数
    this.isNegativeInteger = function(value) {
      return this.isLegalValue(value, /^[-]{1}\d+$/);
    };
    // value是否浮点数
    this.isFloat = function(value) {
      return this.isInteger(value) || this.isLegalValue(value, /^[-\+]?\d+(\.\d+)?$/);
    };
    // value是否正浮点数
    this.isPositiveFloat = function(value) {
      return this.isPositiveInteger(value) || this.isLegalValue(value, /^[\+]?\d+(\.\d+)?$/);
    };
    // value是否负浮点数
    this.isNegativeFloat = function(value) {
      return this.isNegativeInteger(value) || this.isLegalValue(value, /^[-]+\d+(\.\d+)?$/);
    };
    // value是否小于maxValue
    this.isLessThan = function(value, maxValue) {
      if ((parseFloat(value) | Number.POSITIVE_INFINITY) < maxValue)
        return true;
      return false;
    };
    // value是否小于等于maxValue
    this.isLessThanEq = function(value, maxValue) {
      if ((parseFloat(value) | Number.POSITIVE_INFINITY) <= maxValue)
        return true;
      return false;
    };
    // value是否大于minValue
    this.isGreaterThan = function(value, minValue) {
      if ((parseFloat(value) | Number.NEGATIVE_INFINITY) > minValue)
        return true;
      return false;
    };
    // value是否大于等于minValue
    this.isGreaterThanEq = function(value, minValue) {
      if ((parseFloat(value) | Number.NEGATIVE_INFINITY) >= minValue)
        return true;
      return false;
    };
    // value是否大于等于minValue并且小于等于maxValue
    this.isBetween = function(value, minValue, maxValue) {
      if (minValue > (parseFloat(value) | Number.POSITIVE_INFINITY) || ((parseFloat(value) | Number.NEGATIVE_INFINITY)) > maxValue) {
        return false;
      } else {
        return true;
      }
    };
  };
  $.ValidateUtil = new ValidateUtil();
})(jQuery);