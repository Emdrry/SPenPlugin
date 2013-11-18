var SPenPlugin = { 
    callNativeFunction: function (success, fail, args) { 
      	return cordova.exec(success,fail,"SPenPlugin","nativeAction", args ); 
    } 
};


