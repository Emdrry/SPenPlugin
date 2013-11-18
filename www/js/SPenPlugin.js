var SPenPlugin = { 
    callNativeFunction: function (success, fail, args) { 
      	cordova.exec(success,fail,"SPenPlugin","nativeAction", args ); 
    } 
};


