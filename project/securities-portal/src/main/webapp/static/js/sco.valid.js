/* ==========================================================
 * sco.valid.js
 * http://github.com/terebentina/sco.js
 * ==========================================================
 * Copyright 2013 Dan Caragea.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================== */

/*jshint laxcomma:true, sub:true, browser:true, jquery:true, smarttabs:true */

;(function($, undefined) {
	"use strict";

	var pluginName = 'scojs_valid';

	function Valid($form, options) {
		this.options = $.extend({}, $.fn[pluginName].defaults, options);
		this.$form = $form;
		this.allowed_rules = [];
		this.errors = {};
		var self = this;
		$.each(this.methods, function(k, v) {
			self.allowed_rules.push(k);
		});
	}

	$.extend(Valid.prototype, {
		// this is the main function - it returns either true if the validation passed or false
		validate: function() {

			var  self = this
			,form_fields = this.$form.find(':input[name]:visible').serializeArray()
			,all_fields = this.$form.find(':input[name]:visible').map(function() {return this.name;}).get()
			;

			// remove any possible displayed errors from previous runs
			$.each(this.errors, function(field_name, error) { 
				var $input = self.$form.find('[name="' + field_name + '"]');
				$input.siblings('span.help-block').html('');
				if (self.options.wrapper !== null) {
					$input.closest(self.options.wrapper).removeClass('has-error');
				}
			});
			this.errors = {};

			$.each(self.options.rules, function(field_name, rules) {
				var  field = null
					,normalized_rules = {}
					;
				// find the field in the form
				$.each(form_fields, function(k, v) {
					if (v.name === field_name) {
						field = v;
						return false;
					}
				});

				// if field was not found, it could mean 2 things: mispelled field name in the rules or the field is not a successful control
				// even if it's not successful we have to validate it
				if (field === null) {
					if ($.inArray(field_name, all_fields) !== -1) {
						field = {name: field_name, value: self.get_field_value(field_name)};
					}
				}

				// if it's still null then it's either mispelled or disabled. We don't care either way
				if (field !== null) {
					$.each(rules, function(rule_idx, rule_value) {
						// determine the method to call and its args
						// only string and objects are allowed
						if ($.type(rule_value) === 'string') {
							// make sure the requested method actually exists.
							if ($.inArray(rule_value, self.allowed_rules) !== -1) {
								normalized_rules[rule_value] = null;
							}
						} else {
							// if not string then we assume it's a {key: val} object. Only 1 key is allowed
							$.each(rule_value, function(k, v) {
								// make sure the requested method actually exists.
								if ($.inArray(k, self.allowed_rules) !== -1) {
									normalized_rules[k] = v;
									return false;
								}
							});
						}
					});

					$.each(normalized_rules, function(fn_name, fn_args) {
						// call the method with the requested args
						if (self.methods[fn_name].call(self, field.name, field.value, fn_args, normalized_rules) !== true) {
							self.errors[field.name] = self.format.call(self, field.name, fn_name, fn_args);
						}
					});
				}
			});

			if (!$.isEmptyObject(this.errors)) {
				this.show(this.errors);
				return false;
			} else {
				return true;
			}
		},


		show: function(errors) {
			var self = this;
			$.each(errors, function(k, v) {
				var $input = self.$form.find('[name="' + k + '"]'),
					$span = $input.siblings('.help-block');
				if (self.options.wrapper !== null) {
					$input.closest(self.options.wrapper).addClass('has-error');
				}
				if ($span.length === 0) {
					$span = $('<span/>', {'class': 'help-block'});
					if($input.offsetParent().hasClass('input-group')){
						if($input.offsetParent().siblings('.help-block').length === 0){
							//json回调初始数据
							$input.offsetParent().after($span);
						}else{
							//json回调覆盖之前数据
							$input.offsetParent().siblings('.help-block').replaceWith($span);
						}
					}else{
						$input.after($span);
					}

				}
				$span.html(v);
				$input.focus();
				if(k == 'code'){
					if($("#code").val() !=''){
						$("#secode_img").trigger('click');
						$("#code").val("");
					}
				}

			});
			//取消提交按钮禁用状态
			self.$form.find('[type="submit"]').removeAttr('disabled');
		},


		methods: {
			not_empty: function(field, value) {
				return value !== null && $.trim(value).length > 0;
			},name_only: function(field, value) {
				var name_count="0",result;
				if ($.trim(value).length === 0) {
					result =true;
				}else{
					$.ajax({
						type: 'POST',
						url: base_url+'/member/name_only',
						data:{'name':value},
						async: false,
						success: function(data){
							name_count = (eval('('+data+')')).namecount;
						}
					});
					result = (name_count==="0");
				}
				return result;
			},
			codeequal: function(field, value) {
				var code = value.toUpperCase(),co="",result;
				if ($.trim(value).length === 0) {
					result=true;
				}else{
				$.ajax({
					type: 'POST',
					url: base_url+'/member/sesscodeget',
					data:{'code':code},
					async: false,
					success: function(data){
						co = (eval('('+data+')')).vars;
					}
				});
				result = (co === "Y") ;}
				return result;
			},
			mobile_only: function(field, value) {
				var co="",result;
				if ($.trim(value).length === 0) {
					result=true;
				}else{
					$.ajax({
						type: 'POST',
						url: base_url+'/member/mobile_only',
						data:{'mobile':value},
						async: false,
						success: function(data){
							co = (eval('('+data+')')).vars;
						}
					});
					result = (co === "Y") ;}
				return result;
			},
			email_only: function(field, value) {
				var co="",result;
				if ($.trim(value).length === 0) {
					result=true;
				}else{
					$.ajax({
						type: 'POST',
						url: base_url+'/member/email_only',
						data:{'email':value},
						async: false,
						success: function(data){
							co = (eval('('+data+')')).vars;
						}
					});
					result = (co === "Y") ;}
				return result;
			},
			pass_right: function(field, value) {
				var result,co;
				if ($.trim(value).length === 0) {
					result=true;
				}else{
					var name = $("#username").val();
					$.ajax({
						type: 'POST',
						url: base_url+'/pass_right',
						data:{'name':name,'passwd':value},
						async: false,
						success: function(data){
							co = (eval('('+data+')')).vars;
						}
					});
					result = (co === "Y") ;}
				return result;
			},

			min_length: function(field, value, min_len, all_rules) {
				var length = $.trim(value).length
					,result = (length >= min_len);
				if (!all_rules['not_empty']) {
					result = result || length === 0;
				}
				return result;
			},
			idcard_format: function(field,value) {
				var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
				var fmid = /^[1-9][0-9]{5}(19[0-9]{2}|200[0-9]|2010)(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9xX]$/i;
				var result = true;

				if($.trim(value).length===0){
					result=true;
				}else if($.trim(value).length<15||$.trim(value).length>18){
					result=true;
				}else if(!fmid.test(value)){
					result=false;
				}else if(!city[value.substr(0,2)]){
					result=false;
				}else{
					//18位身份证需要验证最后一位校验位
					if($.trim(value).length === 18){
						value = value.split('');
						//∑(ai×Wi)(mod 11)
						//加权因子
						var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
						//校验位
						var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
						var sum = 0;
						var ai = 0;
						var wi = 0;
						for (var i = 0; i < 17; i++)
						{
							ai = value[i];
							wi = factor[i];
							sum += ai * wi;
						}
						var last = parity[sum % 11];
						if(parity[sum % 11] != value[17]){
							result = false;
						}
					}
				}
				return result;
			},
			max_length: function(field, value, max_len) {
				return $.trim(value).length <= max_len;
			},

			regex: function(field, value, regexp) {
				return regexp.test(value);
			},
			mobile: function(field, value) {
				var result;
				if ($.trim(value).length === 0) {
					result =true;
				}else{
				var reg = /^0?1[3|4|5|8][0-9]\d{8}$/;
				result =  reg.test(value);
				}
				return result;
			},
			email: function(field, value) {
				// by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/
				var regex = /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i;
				var result;
				if ($.trim(value).length === 0) {
					result =true;
				}else{
				result = regex.test($.trim(value));
				}
				return result;
			},

			url: function(field, value) {
				// by Scott Gonzalez: http://projects.scottsplayground.com/iri/
				var regex = /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i;
				return regex.test(value);
			},

			exact_length: function(field, value, exact_length, all_rules) {
				var  length = $.trim(value).length
					,result = (length === exact_length);
				if (!all_rules['not_empty']) {
					result = result || length === 0;
				}
				return result;
			},

			equals: function(field, value, target) {
				return value === target;
			},
			Idoagree: function(field, value, target) {
				return $("#"+field).attr('checked')==="checked";
			},
			ip: function(field, value) {
				var regex = /^((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})$/i;
				return regex.test($.trim(value));
			},

			credit_card: function(field, value) {
				// accept only spaces, digits and dashes
				if (/[^0-9 \-]+/.test(value)) {
					return false;
				}
				var  nCheck = 0
					,nDigit = 0
					,bEven = false;

				value = value.replace(/\D/g, "");

				for (var n = value.length - 1; n >= 0; n--) {
					var cDigit = value.charAt(n);
					nDigit = parseInt(cDigit, 10);
					if (bEven) {
						if ((nDigit *= 2) > 9) {
							nDigit -= 9;
						}
					}
					nCheck += nDigit;
					bEven = !bEven;
				}

				return (nCheck % 10) === 0;
			},

			alpha: function(field, value) {
				var regex = /^[a-z]*$/i;
				return regex.test(value);
			},

			alpha_numeric: function(field, value) {
				var regex = /^[a-z0-9]*$/i;
				return regex.test(value);
			},

			alpha_dash: function(field, value) {
				var regex = /^[a-z0-9_\-]*$/i;
				return regex.test(value);
			},

			digit: function(field, value) {
				var regex = /^\d*$/;
				return regex.test(value);
			},

			numeric: function(field, value) {
				var regex = /^([\+\-]?[0-9]+(\.[0-9]+)?)?$/;
				return regex.test(value);
			},

			// same as numeric
			decimal: function(field, value) {
				var regex = /^([\+\-]?[0-9]+(\.[0-9]+)?)?$/;
				return regex.test(value);
			},

			matches: function(field, value, param) {
				return value === this.$form.find('[name="' + param + '"]').val();
			},
			haveprovince: function(field, value) {
				return value !== "p";
			},
			havecity: function(field, value) {
				return value !== "c";
			},
			passwordvalidate: function(field, value) {
				var result,co;
				if ($.trim(value).length === 0||$.trim(value).length<5) {
					result=true;
				}else{
					$.ajax({
						type: 'POST',
						url: base_url+'/member/passwordvalidate',
						data:{'pd':value},
						async: false,
						success: function(data){
							co = (eval('('+data+')')).yn;
						}
					});
					result = (co === "Y") ;}
				return result;
			},
			paypasswordvalidate: function(field, value) {
				var result,co;
				if ($.trim(value).length === 0||$.trim(value).length<5) {
					result=true;
				}else{
					$.ajax({
						type: 'POST',
						url: base_url+'/member/paypasswordvalidate',
						data:{'pd':value},
						async: false,
						success: function(data){
							co = (eval('('+data+')')).yn;
						}
					});
					result = (co === "Y") ;}
				return result;
			}
		},


		messages: {
			not_empty: 'This field is required.'
			,min_length: 'Please enter at least :value characters.'
			,max_length: 'Please enter no more than :value characters.'
			,regex: ''
			,email: 'Please enter a valid email address.'
			,url: 'Please enter a valid URL.'
			,exact_length: 'Please enter exactly :value characters.'
			,equals: ''
			,ip: ''
			,credit_card: 'Please enter a valid credit card number.'
			,alpha: ''
			,alpha_numeric: ''
			,alpha_dash: ''
			,digit: 'Please enter only digits.'
			,numeric: 'Please enter a valid number.'
			,decimal: 'Please enter a decimal number.'
			,matches: 'Must match the previous value.'
		},


		/**
		 * finds the most specific error message string and replaces any ":value" substring with the actual value
		 */
		format: function(field_name, rule, params) {
			var message;
			if (typeof this.options.messages[field_name] !== 'undefined' && typeof this.options.messages[field_name][rule] !== 'undefined') {
				message = this.options.messages[field_name][rule];
			} else {
				message = this.messages[rule];
			}

			if ($.type(params) !== 'undefined' && params !== null) {
				if ($.type(params) === 'boolean' || $.type(params) === 'string' || $.type(params) === 'number') {
					params = {value: params};
				}
				$.each(params, function(k, v) {
					message = message.replace(new RegExp(':' + k, 'ig'), v);
				});
			}
			return message;
		},


		/**
		 * get a normalized value for a form field.
		 */
		get_field_value: function(field_name) {
			var $input = this.$form.find('[name="' + field_name + '"]');
			if ($input.is('[type="checkbox"], [type="radio"]')) {
				return $input.is(':checked') ? $input.val() : null;
			} else {
				return $input.val();
			}
		}
	});


	/**
	 * main function to use on a form (like $('#form).scojs_valid({...})). Performs validation of the form, sets the error messages on form inputs and returns
	 * true/false depending on whether the form passed validation or not
	 *
	 * @param hash/string options the hash of rules and messages to validate the form against (and messages to show if failed validation) or the string "option"
	 * @param {string} key the option key to retrieve or set. If the third param of the function is available then act as a setter, otherwise as a getter.
	 * @param {mixed} value the value to set on the key
	 */
	$.fn[pluginName] = function(options, key, value) {
		var  $form = this.eq(0)
			,validator = $form.data(pluginName);
		if ($.type(options) === 'object') {
			if (!validator) {
				var opts = $.extend({}, $.fn[pluginName].defaults, options, $form.data());
				validator = new Valid($form, opts);
				$form.data(pluginName, validator).attr('novalidate', 'novalidate');
			}
			$form.ajaxForm({
				beforeSerialize: function() {
					if (typeof validator.options.onBeforeValidate === 'function') {
						validator.options.onBeforeValidate.call(validator);
					}
				}
				,beforeSubmit: function() {
					var returnValid = validator.validate();
					if(returnValid){
						$form.find('[type="submit"]').attr('disabled',true);
					}
					return returnValid;
				}
				,dataType: 'json'
				,success: function(response, status, xhr, $form) {
					if (response.status === 'error') {
						if (typeof options.onError !== 'function' || options.onError.call(this, response, validator, $form) !== false) {
							validator.show(response.data.errors);
						}
					} else if (response.status === 'fail') {
						if (typeof options.onFail !== 'function' || options.onFail.call(this, response, validator, $form) !== false) {
							//$.scojs_message(response.message, $.scojs_message.TYPE_ERROR);
							$form.find('[type="submit"]').removeAttr('disabled');
							//关闭举报弹窗，延迟提示
							$('.close').trigger('click');
							var stt = setTimeout(function(){
								tipsOp(response.data,response.status);
								clearTimeout(stt);
								if(response.url){
									setTimeout(function(){window.location.href=response.url;},2000);
						  	  	}
							},1000);

						}
					} else if (response.status === 'success') {
						if (typeof options.onSuccess !== 'function' || options.onSuccess.call(this, response, validator, $form) !== false) {
							if(response.data){
								if (response.data.next) {
									if (response.data.next === '.') {			// refresh current page
										window.location.href = window.location.href.replace(/#.*$/, '');
									} else if (response.data.next === 'x') {	// close the parent modal
										$form.closest('.modal').trigger('close');
									} else {
										window.location.href = response.data.next;
									}
								}
								if (response.data.message) {
									$('.close').trigger('click');
									tipsOp(response.data,response.status);
									if(response.url){
										setTimeout(function(){window.location.href=response.url;},2000);
									}
									//$.scojs_message(response.data.message, $.scojs_message.TYPE_OK);
								}
							}
						}
					}
				}
			});
			// allow chaining
			return this;
		} else if (options === 'option') {
			if ($.type(value) === 'undefined') {
				return validator.options[key];
			} else {
				validator.options[key] = value;
				return validator;
			}
		} else {
			return validator ? validator : this;
		}
	};


	$[pluginName] = function(form, options) {
		if (typeof form === 'string') {
			form = $(form);
		}
		return new Valid(form, options);
	};


	$.fn[pluginName].defaults = {
		wrapper: 'label'	// the html tag that wraps the field and which defines a "row" of the form
		,rules: {}			// array of rules to check the form against. Each value should be either a string with the name of the method to use as rule or a hash like {method: <method params>}
		,messages: {}		// custom error messages like {username: {not_empty: 'hey you forgot to enter your username', min_length: 'come on, more than 2 chars, ok?'}, password: {....}}
	};
})(jQuery);
