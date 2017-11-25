"use strict";
const app = angular.module('demoAppModule', ['ui.bootstrap']);
app.config(['$qProvider', function ($qProvider) {
    $qProvider.errorOnUnhandledRejections(false);
}]);

app.controller('AgreementController', function($http, $scope, $location, $uibModal, $rootScope) {
    const demoApp = this;

    // We identify the node.
    $rootScope.apiBaseURL = "/api/template/";
    $rootScope.peers = [];
	let agreements = [];

	$http.get($rootScope.apiBaseURL + "getAgreements").then((response) => agreements = response.data.me);
	$http.get($rootScope.apiBaseURL + "peers").then((response) => $rootScope.peers = response.data.peers);

	$rootScope.collateral = [{id: 1,name:"CASH"},{id: 2,name:"SECURITIES"}];
	$rootScope.threshold = [{id: 1,name:"AAA"},{id: 2,name:"AA+ to AA-"},{id: 3,name:"A+ to A-"},{id: 4,name:"BBB+ or below"}];
	$rootScope.yesNo = [{id: 1,name:"Yes"},{id: 0,name:"No"}];
	$rootScope.deliveryAmount = [{id: 1,name:"Party A only pays"},{id: 2,name:"Party B only pays"},{id: 3,name:"Both Party A and Party B pay"}];
	$rootScope.moodys = [{id: 1,name:"AAA"},{id: 2,name:"AA1 to AA3"},{id: 3,name:"A1 to A3"},{id: 4,name:"Baa1 or below"}];
	$rootScope.sps = [{id: 1,name:"AAA"},{id: 2,name:"AA+ to AA-"},{id: 3,name:"A+ to A-"},{id: 4,name:"BBB+ or below"}];
	$rootScope.fitchs = [{id: 1,name:"AAA"},{id: 2,name:"AA"},{id: 3,name:"A"},{id: 4,name:"BBB"},{id: 5,name:"BB"},{id: 6,name:"CCC"},{id: 7,name:"D"}];

    demoApp.openModal = () => {
        const modalInstance = $uibModal.open({
            templateUrl: 'initiateAgreementModal.html',
            controller: 'ModalInstanceCtrl',
            controllerAs: 'modalInstance',
            resolve: {
                peers: () => $rootScope.peers
            }
        });
        modalInstance.result.then(() => {}, () => {});
    };

	demoApp.viewAgreement = (agreement) => {
		const modalInstance = $uibModal.open({
			templateUrl: 'viewAgreementModal.html',
			controller: 'AgreementCtrl',
			controllerAs: 'AgreementCtrl',
			resolve: {
				agreement: () => agreement
			}
		});
		modalInstance.result.then(() => {}, () => {});
    };

	demoApp.amendAgreement = (agreement) => {
        const modalInstance = $uibModal.open({
            templateUrl: 'amendAgreementModal.html',
            controller: 'AmendAgreementCtrl',
            controllerAs: 'AmendAgreementCtrl',
            resolve: {
                agreement: () => agreement,
				deliveryAmount: () => $rootScope.deliveryAmount,
				collateral: () => $rootScope.collateral,
				threshold: () => $rootScope.threshold,
				peers: () => $rootScope.peers,
				yesNo: () => $rootScope.yesNo
            }
        });

        modalInstance.result.then(() => {}, () => {});
    };


	demoApp.viewAudit = (agreement) => {
        $http.get($rootScope.apiBaseURL + "audit?agreementName=" + agreement.agrementName)
        .then(function(response) {
			demoApp.agreementAudits = response.data;
		});
    };

	demoApp.getAgreements = () => $http.get($rootScope.apiBaseURL + "getAgreements")
        .then(function(response) {
			demoApp.agreements = response.data;
		});
	//demoApp.agreements = [{id:1}]; //SanjayTest


    demoApp.getAgreements();
	$scope.getDateValue = (dt) => {
		var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
		if (dt != null && dt != undefined && dt != '') {
			var createdDate = new Date(dt);
			var day = createdDate.getDate();
			var month = createdDate.getMonth();
			var year = createdDate.getFullYear();
			var time = createdDate.toLocaleTimeString().replace(/(.*)\D\d+/, '$1');

			var formattedDate = day + '-' + months[month] + '-' + year;
			return formattedDate;
		} else {
			return 'NA';
		}
    };
	$rootScope.convertToDt = (str) => {
		if(str instanceof Date)
		{

		}
		else {
			if (str != null && str != undefined && str != '') {
				var createdDate = new Date(str);
				return createdDate;
			} else {
				return new Date();
			}
		}
    };
});

app.controller('ModalInstanceCtrl', function ($scope, $rootScope, $http, $location, $uibModalInstance, $uibModal, peers) {
    const modalInstance = this;

	$scope.moodys = $rootScope.moodys;
	$scope.sps = $rootScope.sps;
	$scope.fitchs = $rootScope.fitchs;

    modalInstance.peers = peers;
    modalInstance.form = {baseCurrency: 'GBP',valuationPercentageCash:100};
    $scope.agreement = {baseCurrency: 'GBP',valuationPercentageCash:100, eligibleCollaterals: [], thresholds: [],specifiedConditions: []};

    modalInstance.formError = false;
	modalInstance.deliveryAmount = $rootScope.deliveryAmount;
	modalInstance.collateral = $rootScope.collateral;
	modalInstance.threshold = $rootScope.threshold;
	modalInstance.yesNo = $rootScope.yesNo;

	$scope.changedValue = () => {
		console.log($scope.agreement.eligibleCollateral.id);
		if($scope.agreement.eligibleCollateral.id == 1){
			const cashCtrlInstance = $uibModal.open({
				templateUrl: 'eligibleColCash.html',
				controller: 'cashCtrl',
				controllerAs: 'cashCtrl',
				resolve: {
					apiBaseURL: () => $rootScope.apiBaseURL,
					agreementModel: () => $scope.agreement,
					data: () => null
				}
			});
			cashCtrlInstance.result.then(() => {}, () => {});
		} else {
			const secCtrlInstance = $uibModal.open({
				templateUrl: 'eligibleColSec.html',
				controller: 'secCtrl',
				controllerAs: 'secCtrl',
				resolve: {
					apiBaseURL: () => $rootScope.apiBaseURL,
					agreementModel: () => $scope.agreement,
					data: () => null
				}
			});
			secCtrlInstance.result.then(() => {}, () => {});
		}

	}

	$scope.addThresholds = () => {
		const thresholdsCtrlInstance = $uibModal.open({
            templateUrl: 'addThresholds.html',
            controller: 'thresholdsCtrl',
            controllerAs: 'thresholdsCtrl',
            resolve: {
                apiBaseURL: () => $rootScope.apiBaseURL,
				agreementModel: () => $scope.agreement,
				data: () => null
            }
        });
		thresholdsCtrlInstance.result.then(() => {}, () => {});
	}
	$scope.addConditions = (id) => {
		if(id == 1)
			$scope.agreement.specifiedConditions.push("Illegality");
		else if(id ==2)
			$scope.agreement.specifiedConditions.push("Credit Event Upon Merger");
		else if(id ==3)
			$scope.agreement.specifiedConditions.push("Additional Termination Events");
	}
    // Validate and create IOU.
    modalInstance.create = () => {
		console.log('Called Create');
		modalInstance.formError = false;
		$uibModalInstance.close();
		var agreement = {
			agrementName:$scope.agreement.agrementName,
			counterparty:$scope.agreement.counterparty,
			baseCurrency:$scope.agreement.baseCurrency,
			eligibleCurrency:$scope.agreement.eligibleCurrency,
			deliveryAmount:$scope.agreement.deliveryAmount,
			returnAmount:$scope.agreement.returnAmount,
			creditSupportAmount:$scope.agreement.creditSupportAmount,

			eligibleCollaterals:$scope.agreement.eligibleCollaterals,
			thresholds:$scope.agreement.thresholds,
			specifiedConditions:$scope.agreement.specifiedConditions,

			initialMargin:$scope.agreement.initialMargin,
			valuationAgent:$scope.agreement.valuationAgent,
			valuationDate:$scope.agreement.valuationDate,
			valuationTime:$scope.agreement.valuationTime,
			notificationTime:$scope.agreement.notificationTime,
			substitutionDateFrom:$scope.agreement.substitutionDateFrom,
			substitutionDateTo:$scope.agreement.substitutionDateTo,
			consent:$scope.agreement.consent
		};
		$rootScope.dummy = agreement;
		console.log(agreement);
		const createIOUEndpoint = $rootScope.apiBaseURL + "initFlow/" +$scope.agreement.counterparty;

		// Create PO and handle success / fail responses.
		$http.put(createIOUEndpoint, angular.toJson(agreement)).then(
			(result) => modalInstance.displayMessage(result),
			(result) => modalInstance.displayMessage(result)
		);

            // Create PO and handle success / fail responses.
            $http.put(createIOUEndpoint, angular.toJson(modalInstance.form)).then(
                (result) => modalInstance.displayMessage(result),
                (result) => modalInstance.displayMessage(result)
            );
        }
    };

    modalInstance.displayMessage = (message) => {
        const modalInstanceTwo = $uibModal.open({
            templateUrl: 'messageContent.html',
            controller: 'messageCtrl',
            controllerAs: 'modalInstanceTwo',
            resolve: { message: () => message }
        });

        // No behaviour on close / dismiss.
        modalInstanceTwo.result.then(() => {}, () => {});
    };

    // Close create IOU modal dialogue.
    modalInstance.cancel = () => $uibModalInstance.dismiss();

    // Validate the IOU.
    function invalidFormInput() {
        return ($scope.agreement.counterparty === undefined);
    }
});

app.controller('AgreementCtrl', function ($scope, $rootScope, $http, $location, $uibModalInstance, $uibModal, agreement) {
    const modalInstance = this;
	//$scope.agreement = $rootScope.dummy; //SanjayTest
    $scope.agreement = agreement;
	// Validate and create IOU.
    $scope.agree = (agreement) => {
		console.log('Called Create '+agreement);
		var updAgreement ={
			agrementName: agreement.agrementName,
			agreementValue: agreement.agreementValue,
			collateral: agreement.collateral
		}

		$uibModalInstance.close();
		const createIOUEndpoint = $rootScope.apiBaseURL + "acceptFlow/";
		// Create PO and handle success / fail responses.
		$http.put(createIOUEndpoint, angular.toJson(updAgreement)).then(
			(result) => $scope.displayMessage(result),
			(result) => $scope.displayMessage(result)
		);
		//$scope.cancel();
    };
	$scope.getAmtValue = (id) => {
		var v = $rootScope.deliveryAmount.find(x => x.id === id);
		return v.name;
    };
	$scope.getDateValue = (dt) => {
		var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
		if (dt != null && dt != undefined && dt != '') {
			var createdDate = new Date(dt);
			var day = createdDate.getDate();
			var month = createdDate.getMonth();
			var year = createdDate.getFullYear();
			var time = createdDate.toLocaleTimeString().replace(/(.*)\D\d+/, '$1');

			var formattedDate = day + '-' + months[month] + '-' + year;
			return formattedDate;
		} else {
			return 'NA';
		}
    };

    $scope.displayMessage = (message) => {
        const modalInstanceTwo = $uibModal.open({
            templateUrl: 'messageContent.html',
            controller: 'messageCtrl',
            controllerAs: 'modalInstanceTwo',
            resolve: { message: () => message }
        });

        // No behaviour on close / dismiss.
        modalInstanceTwo.result.then(() => {}, () => {});
    };

    // Close create IOU modal dialogue.
    $scope.cancel = () => $uibModalInstance.dismiss();
});

app.controller('AmendAgreementCtrl', function ($scope, $rootScope, $http, $location, $uibModalInstance, $uibModal, agreement,deliveryAmount,collateral,threshold,peers,yesNo) {
    const modalInstance = this;
	$scope.deliveryAmount=deliveryAmount;
	$scope.collateral= collateral;
	$scope.threshold= threshold;
	$scope.peers= peers;
	$scope.yesNo= yesNo;

	//$scope.agreement = $rootScope.dummy; //SanjayTest
    $scope.agreement = agreement;
	$scope.agreement.substitutionDateFrom = $rootScope.convertToDt($scope.agreement.substitutionDateFrom);
	$scope.agreement.substitutionDateTo = $rootScope.convertToDt($scope.agreement.substitutionDateTo);
	// Validate and create IOU.
    $scope.amendAgreement = (agreement) => {
		console.log('Called Create ',agreement);
		const updatedAgreement = {
			agrementName: agreement.agrementName,
			agreementValue: agreement.agreementValue,
			collateral: agreement.collateral
		};

		$uibModalInstance.close();
		const createIOUEndpoint = $rootScope.apiBaseURL + "amendFlow/" +$scope.agreement.counterparty;
		// Create PO and handle success / fail responses.
		$http.put(createIOUEndpoint, angular.toJson(agreement)).then(
			(result) => $scope.displayMessage(result),
			(result) => $scope.displayMessage(result)
		);
		//$scope.cancel();
    };

	$scope.editCollateral = (data) => {
		if(data.collateralType == 1){
			const cashCtrlInstance = $uibModal.open({
				templateUrl: 'eligibleColCashUpd.html',
				controller: 'cashUpdCtrl',
				controllerAs: 'cashUpdCtrl',
				resolve: {
					apiBaseURL: () => $rootScope.apiBaseURL,
					agreementModel: () => $scope.agreement,
					data: () => data
				}
			});
			cashCtrlInstance.result.then(() => {}, () => {});
		} else {
			const secCtrlInstance = $uibModal.open({
				templateUrl: 'eligibleColSecUpd.html',
				controller: 'secUpdCtrl',
				controllerAs: 'secUpdCtrl',
				resolve: {
					apiBaseURL: () => $rootScope.apiBaseURL,
					agreementModel: () => $scope.agreement,
					data: () => data
				}
			});
			secCtrlInstance.result.then(() => {}, () => {});
		}
    };
	$scope.deleteCollateral = (item) => {

		var index = $scope.agreement.eligibleCollaterals.indexOf(item);
		if(index != -1)
			$scope.agreement.eligibleCollaterals.splice(index, 1);

    };

	$scope.editThresholds = (data) => {
        const thresholdsCtrlInstance = $uibModal.open({
            templateUrl: 'addThresholdsUpd.html',
            controller: 'thresholdsUpdCtrl',
            controllerAs: 'thresholdsUpdCtrl',
            resolve: {
                apiBaseURL: () => $rootScope.apiBaseURL,
				agreementModel: () => $scope.agreement,
				data: () => data
            }
        });
		thresholdsCtrlInstance.result.then(() => {}, () => {});
    };
	$scope.deleteThresholds = (item) => {
        var index = $scope.agreement.thresholds.indexOf(item);
		if(index != -1)
			$scope.agreement.thresholds.splice(index, 1);
    };

	$scope.addConditions = (id) => {
		if(id == 1)
			$scope.agreement.specifiedConditions.push("Illegality");
		else if(id ==2)
			$scope.agreement.specifiedConditions.push("Credit Event Upon Merger");
		else if(id ==3)
			$scope.agreement.specifiedConditions.push("Additional Termination Events");
	}
	$scope.getCond = (id) => {
		if(id == 1)
			return $scope.agreement.specifiedConditions.indexOf('Illegality') > -1;
		else if(id ==2)
			return $scope.agreement.specifiedConditions.indexOf('Credit Event Upon Merger') > -1;
		else if(id ==3)
			return $scope.agreement.specifiedConditions.indexOf('Additional Termination Events') > -1;
	}


    $scope.displayMessage = (message) => {
        const modalInstanceTwo = $uibModal.open({
            templateUrl: 'messageContent.html',
            controller: 'messageCtrl',
            controllerAs: 'modalInstanceTwo',
            resolve: { message: () => message }
        });

        // No behaviour on close / dismiss.
        modalInstanceTwo.result.then(() => {}, () => {});
    };


    // Close create IOU modal dialogue.
    $scope.cancel = () => $uibModalInstance.dismiss();
});

// Controller for success/fail modal dialogue.
app.controller('messageCtrl', function ($uibModalInstance, message) {
    const modalInstanceTwo = this;
    modalInstanceTwo.message = message.data;
});

app.controller('cashCtrl', function ($scope,$rootScope, $http, $location, $uibModalInstance, $uibModal, apiBaseURL, agreementModel, data) {

    $scope.el = {collateralType: 1, currency: "", moody: "",sp: "",fitch:"",rfrom:"",rto:"",valuation:100,remMaturity:"",remMaturityFrom:"",remMaturityTo:"",partyA: false,partyB: false};
	$scope.elNew = {collateralType: 1, currency: "", moody: "",sp: "",fitch:"",rfrom:"",rto:"",valuation:100,remMaturity:"",remMaturityFrom:"",remMaturityTo:"",partyA: false,partyB: false};
	$scope.yesNo = $rootScope.yesNo;

	$scope.cash = [];
	if(data != null){
		$scope.el.currency = data.currency;
		$scope.el.valuation = data.amount;
		$scope.el.partyA = data.partyA === 1?true:false;
		$scope.el.partyB = data.partyB === 1?true:false;
	}

	$scope.submit = (el) => {
		console.log('Called Create '+el);
		//$scope.cash.push(el);
		//agreementModel.eligibleCollaterals.push(el);
		$uibModalInstance.dismiss();
    };
	$scope.add = (el) => {
		$scope.cash.push(el);
		var eligibleCash = {
			collateralType: 1,
			currency: el.currency,
			ratingType: 0,
			rating: 0,
			ratingText: "",
			ratingRangeFrom: 0,
			ratingRangeTo: 0,
			amount: el.valuation,
			remainingMaturity: 0,
			remMaturityFrom: 0,
			remMaturityTo: 0,
			partyA: el.partyA ? 1:0,
			partyB: el.partyB ? 1:0
		}
		console.log('Adding Cash EC ',eligibleCash);
		agreementModel.eligibleCollaterals.push(eligibleCash);
		$scope.el = {collateralType: 1, currency: "", moody: "",sp: "",fitch:"",rfrom:"",rto:"",valuation:100,remMaturity:"",remMaturityFrom:"",remMaturityTo:"",partyA: false,partyB: false};
    };
	$scope.cancel = () => {
		agreementModel.eligibleCollaterals.splice(0, agreementModel.eligibleCollaterals.length);
		$uibModalInstance.dismiss();
    };
});

app.controller('cashUpdCtrl', function ($scope,$rootScope, $http, $location, $uibModalInstance, $uibModal, apiBaseURL, agreementModel, data) {

    $scope.el = {collateralType: 1, currency: "", moody: "",sp: "",fitch:"",rfrom:"",rto:"",valuation:100,remMaturity:"",remMaturityFrom:"",remMaturityTo:"",partyA: false,partyB: false};
	$scope.elNew = {collateralType: 1, currency: "", moody: "",sp: "",fitch:"",rfrom:"",rto:"",valuation:100,remMaturity:"",remMaturityFrom:"",remMaturityTo:"",partyA: false,partyB: false};
	$scope.yesNo = $rootScope.yesNo;

	$scope.cash = [];
	if(data != null){
		$scope.el.currency = data.currency;
		$scope.el.valuation = data.amount;
		$scope.el.partyA = data.partyA === 1?true:false;
		$scope.el.partyB = data.partyB === 1?true:false;
	}
	//var index = $scope.agreement.eligibleCollaterals.indexOf(data);
	$scope.submit = (el) => {
		console.log('Called Create '+el);
		//$scope.cash.push(el);
		//agreementModel.eligibleCollaterals.push(el);
		$uibModalInstance.dismiss();
    };
	$scope.update = (el) => {

		var eligibleCash = {
			collateralType: 1,
			currency: el.currency,
			ratingType: 0,
			rating: 0,
			ratingText: "",
			ratingRangeFrom: 0,
			ratingRangeTo: 0,
			amount: el.valuation,
			remainingMaturity: 0,
			remMaturityFrom: 0,
			remMaturityTo: 0,
			partyA: el.partyA ? 1:0,
			partyB: el.partyB ? 1:0
		}
		//agreementModel.eligibleCollaterals.push(eligibleCash);
		$uibModalInstance.dismiss();
		$scope.el = {collateralType: 1, currency: "", moody: "",sp: "",fitch:"",rfrom:"",rto:"",valuation:100,remMaturity:"",remMaturityFrom:"",remMaturityTo:"",partyA: false,partyB: false};
    };
	$scope.cancel = () => {
		$uibModalInstance.dismiss();
    };
});

app.controller('secCtrl', function ($scope, $rootScope, $http, $location, $uibModalInstance, $uibModal, apiBaseURL, agreementModel, data) {
    $scope.el = {collateralType: 2, currency: "", moody: "",sp: "",fitch:"",rfrom:0,rto:0,valuation:0,remMaturity:0,remMaturityFrom:0,remMaturityTo:0,partyA: false,partyB: false};
	$scope.moodys = $rootScope.moodys;
	$scope.sps = $rootScope.sps;
	$scope.fitchs = $rootScope.fitchs;

	$scope.elNew = {collateralType: 2, currency: "", moody: "",sp: "",fitch:"",rfrom:0,rto:0,valuation:0,remMaturity:"No",remMaturityFrom:"",remMaturityTo:"",partyA: false,partyB: false};
	$scope.yesNo = $rootScope.yesNo;

	$scope.sec = [];
	if(data != null){
		$scope.el.rfrom = data.ratingRangeFrom;
		$scope.el.rto = data.ratingRangeTo;
		$scope.el.valuation = data.amount;
		$scope.el.remMaturity = data.remainingMaturity;
		$scope.el.remMaturityFrom = data.remMaturityFrom;
		$scope.el.remMaturityTo = data.remMaturityTo;
		$scope.el.partyA = data.partyA === 1?true:false;
		$scope.el.partyB = data.partyB === 1?true:false;
	}


	$scope.submit = (el) => {
		console.log('Called Create '+el);
		//$scope.sec.push(el);
		//agreementModel.eligibleCollaterals.push(el);
		$uibModalInstance.dismiss();
    };
	$scope.add = (el) => {
		$scope.sec.push(el);
		var eligibleSec = {
			collateralType: 2,
			currency: el.currency,
			ratingType: (el.moody != "" ? 1:(el.sp != "" ? 2:(el.fitch != ""?3:0))),
			rating: (el.moody != "" ? el.moody.id:(el.sp != "" ? el.sp.id:el.fitch.id)),
			ratingText: (el.moody != "" ? el.moody.name:(el.sp != "" ? el.sp.name:el.fitch.name)),
			ratingRangeFrom: el.rfrom,
			ratingRangeTo: el.rto,
			amount: el.valuation,
			remainingMaturity: el.remMaturity,
			remMaturityFrom: el.remMaturityFrom,
			remMaturityTo: el.remMaturityTo,
			partyA: el.partyA ? 1:0,
			partyB: el.partyB ? 1:0
		}
		console.log('Adding Security EC ',eligibleSec);
		agreementModel.eligibleCollaterals.push(eligibleSec);
		$scope.el = {collateralType: 2, currency: "", moody: "",sp: "",fitch:"",rfrom:0,rto:0,valuation:0,remMaturity:0,remMaturityFrom:0,remMaturityTo:0,partyA: false,partyB: false};
    };
	$scope.cancel = () => {
		$uibModalInstance.dismiss();
		agreementModel.eligibleCollaterals.splice(0, agreementModel.eligibleCollaterals.length);
    };
});

app.controller('secUpdCtrl', function ($scope, $rootScope, $http, $location, $uibModalInstance, $uibModal, apiBaseURL, agreementModel, data) {
    $scope.el = {collateralType: 2, currency: "", moody: "",sp: "",fitch:"",rfrom:0,rto:0,valuation:0,remMaturity:0,remMaturityFrom:0,remMaturityTo:0,partyA: false,partyB: false};
	$scope.moodys = $rootScope.moodys;
	$scope.sps = $rootScope.sps;
	$scope.fitchs = $rootScope.fitchs;

	$scope.elNew = {collateralType: 2, currency: "", moody: "",sp: "",fitch:"",rfrom:0,rto:0,valuation:0,remMaturity:"No",remMaturityFrom:"",remMaturityTo:"",partyA: false,partyB: false};
	$scope.yesNo = $rootScope.yesNo;

	$scope.sec = [];
	if(data != null){
		$scope.el.rfrom = data.ratingRangeFrom;
		$scope.el.rto = data.ratingRangeTo;
		$scope.el.valuation = data.amount;
		$scope.el.remMaturity = data.remainingMaturity;
		$scope.el.remMaturityFrom = data.remMaturityFrom;
		$scope.el.remMaturityTo = data.remMaturityTo;
		$scope.el.partyA = data.partyA === 1?true:false;
		$scope.el.partyB = data.partyB === 1?true:false;
	}


	$scope.submit = (el) => {
		console.log('Called Create '+el);
		//$scope.sec.push(el);
		//agreementModel.eligibleCollaterals.push(el);
		$uibModalInstance.dismiss();
    };
	$scope.update = (el) => {
		$scope.sec.push(el);
		var eligibleSec = {
			collateralType: 2,
			currency: el.currency,
			ratingType: (el.moody != "" ? 1:(el.sp != "" ? 2:(el.fitch != ""?3:0))),
			rating: (el.moody != "" ? el.moody.id:(el.sp != "" ? el.sp.id:el.fitch.id)),
			ratingText: (el.moody != "" ? el.moody.name:(el.sp != "" ? el.sp.name:el.fitch.name)),
			ratingRangeFrom: el.rfrom,
			ratingRangeTo: el.rto,
			amount: el.valuation,
			remainingMaturity: el.remMaturity,
			remMaturityFrom: el.remMaturityFrom,
			remMaturityTo: el.remMaturityTo,
			partyA: el.partyA ? 1:0,
			partyB: el.partyB ? 1:0
		}
		console.log('Adding Security EC ',eligibleSec);
		$uibModalInstance.dismiss();
		agreementModel.eligibleCollaterals.push(eligibleSec);
		$scope.el = {collateralType: 2, currency: "", moody: "",sp: "",fitch:"",rfrom:0,rto:0,valuation:0,remMaturity:0,remMaturityFrom:0,remMaturityTo:0,partyA: false,partyB: false};
    };
	$scope.cancel = () => {
		$uibModalInstance.dismiss();
    };
});

app.controller('thresholdsCtrl', function ($scope, $rootScope, $http, $location, $uibModalInstance, $uibModal, apiBaseURL, agreementModel, data) {
    $scope.el = {moody: "",sp: "",fitch:"",rfrom:0,rto:0,valuation:0,partyA: false,partyB: false};
	$scope.moodys = $rootScope.moodys;
	$scope.sps = $rootScope.sps;
	$scope.fitchs = $rootScope.fitchs;
	$scope.yesNo = $rootScope.yesNo;

	$scope.sec = [];

	if(data != null){
		$scope.el.rfrom = data.ratingRangeFrom;
		$scope.el.rto = data.ratingRangeTo;
		$scope.el.valuation = data.amount;
		$scope.el.partyA = data.partyA === 1?true:false;
		$scope.el.partyB = data.partyB === 1?true:false;
	}

	$scope.submit = (el) => {
		//console.log('Called Create '+el);
		//$scope.sec.push(el);
		$uibModalInstance.dismiss();
    };
	$scope.add = (el) => {
		$scope.sec.push(el);
		var threshold = {
			collateralType: 0,
			currency: "",
			ratingType: (el.moody != "" ? 1:(el.sp != "" ? 2:(el.fitch != ""?3:0))),
			rating: (el.moody != "" ? el.moody.id:(el.sp != "" ? el.sp.id:(el.fitch != "" ? el.fitch.id: ""))),
			ratingText: (el.moody != "" ? el.moody.name:(el.sp != "" ? el.sp.name:(el.fitch != "" ? el.fitch.name: ""))),
			ratingRangeFrom: el.rfrom,
			ratingRangeTo: el.rto,
			amount: el.valuation,
			remainingMaturity: 0,
			remMaturityFrom: 0,
			remMaturityTo: 0,
			partyA: el.partyA ? 1:0,
			partyB: el.partyB ? 1:0
		}
		console.log('Adding Threshold ',threshold);
		agreementModel.thresholds.push(threshold);
		$scope.el = {moody: "",sp: "",fitch:"",rfrom:0,rto:0,valuation:0,partyA: false,partyB: false};
    };
	$scope.cancel = () => {
		$uibModalInstance.dismiss();
    };
});

app.controller('thresholdsUpdCtrl', function ($scope, $rootScope, $http, $location, $uibModalInstance, $uibModal, apiBaseURL, agreementModel, data) {
    $scope.el = {moody: "",sp: "",fitch:"",rfrom:0,rto:0,valuation:0,partyA: false,partyB: false};
	$scope.moodys = $rootScope.moodys;
	$scope.sps = $rootScope.sps;
	$scope.fitchs = $rootScope.fitchs;
	$scope.yesNo = $rootScope.yesNo;

	$scope.sec = [];

	if(data != null){
		$scope.el.rfrom = data.ratingRangeFrom;
		$scope.el.rto = data.ratingRangeTo;
		$scope.el.valuation = data.amount;
		$scope.el.partyA = data.partyA === 1?true:false;
		$scope.el.partyB = data.partyB === 1?true:false;
	}

	$scope.update = (el) => {
		var threshold = {
			collateralType: 0,
			currency: "",
			ratingType: (el.moody != "" ? 1:(el.sp != "" ? 2:(el.fitch != ""?3:0))),
			rating: (el.moody != "" ? el.moody.id:(el.sp != "" ? el.sp.id:(el.fitch != "" ? el.fitch.id: ""))),
			ratingText: (el.moody != "" ? el.moody.name:(el.sp != "" ? el.sp.name:(el.fitch != "" ? el.fitch.name: ""))),
			ratingRangeFrom: el.rfrom,
			ratingRangeTo: el.rto,
			amount: el.valuation,
			remainingMaturity: 0,
			remMaturityFrom: 0,
			remMaturityTo: 0,
			partyA: el.partyA ? 1:0,
			partyB: el.partyB ? 1:0
		}

		console.log('Adding Threshold',threshold);
		$uibModalInstance.dismiss();
    };
	$scope.cancel = () => {
		$uibModalInstance.dismiss();
    };
});
