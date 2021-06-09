function login($scope, $http) {
	$scope.doLoginLDAP = function() {
		var req = {
			method : 'POST',
			url : 'op/authentication/login-ldap',
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			},
			transformRequest : function(obj) {
				var str = [];
				for ( var p in obj)
					str.push(encodeURIComponent(p) + "="
							+ encodeURIComponent(obj[p]));
				return str.join("&");
			},
			data : {
				username : document.getElementById("usernameLDAP").value,
				password : document.getElementById("passwordLDAP").value,
				service : document.getElementById("serviceLDAP").value
			}
		};

		$http(req).success(function(data) {
			$scope.result = data;
			$scope.service = document.getElementById("serviceLDAP").value;
		});
	};

	$scope.doLoginRSA = function() {
		var formData = new FormData();
		formData.append("username",
				document.getElementById("usernameRSA").value);
		formData.append("file", document.getElementById("fileRSA").files[0]);
		formData.append("service", document.getElementById("serviceRSA").value);

		var xhr = new XMLHttpRequest();
		xhr.open("POST", "op/authentication/login-rsa");
		xhr.setRequestHeader("boundary", undefined);
		xhr.setRequestHeader("processData", false);
		xhr.setRequestHeader('Accept', 'application/json');
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				$scope
						.$apply(function() {
							$scope.result = JSON.parse(xhr.response);
							$scope.service = document
									.getElementById("serviceRSA").value;
						});

			}
		}
		xhr.send(formData);
	};

	$scope.ldapIsSelected = false;
	$scope.rsaIsSelected = false;
	$scope.selectMethod = function(method) {
		switch (method) {
		case "ldap":
			if (document.getElementById("ldapCheckbox").checked) {
				this.ldapIsSelected = true;
				this.rsaIsSelected = false;
				document.getElementById("rsaCheckbox").checked = false;
			} else {
				this.ldapIsSelected = false;
			}
			break;
		case "rsa":
			if (document.getElementById("rsaCheckbox").checked) {
				this.rsaIsSelected = true;
				this.ldapIsSelected = false;
				document.getElementById("ldapCheckbox").checked = false;
			} else {
				this.rsaIsSelected = false;
			}
			break;
		}
	};

}
