
/**
 * @fileoverview
 * Provides methods for the Hello Endpoints sample UI and interaction with the
 * Hello Endpoints API.
 *
 * @author danielholevoet@google.com (Dan Holevoet)
 */

/** google global namespace for Google projects. */
var google = google || {};

/** devrel namespace for Google Developer Relations projects. */
google.devrel = google.devrel || {};

/** samples namespace for DevRel sample code. */
google.devrel.samples = google.devrel.samples || {};

/** hello namespace for this sample. */
google.devrel.samples.hello = google.devrel.samples.hello || {};

/**
 * Client ID of the application (from the APIs Console).
 * @type {string}
 */
google.devrel.samples.hello.CLIENT_ID =
	'529644969614-k8tukdk143p03vqdl32a0mbi1l1n4eci.apps.googleusercontent.com';

/**
 * Scopes used by the application.
 * @type {string}
 */
google.devrel.samples.hello.SCOPES =
    'https://www.googleapis.com/auth/userinfo.email';

/**
 * Whether or not the user is signed in.
 * @type {boolean}
 */
google.devrel.samples.hello.signedIn = false;

/**
 * Loads the application UI after the user has completed auth.
 */
google.devrel.samples.hello.userAuthed = function() {
  var request = gapi.client.oauth2.userinfo.get().execute(function(resp) {
    if (!resp.code) {
      google.devrel.samples.hello.signedIn = true;
      document.getElementById('signinButton').innerHTML = 'Sign out';
      document.getElementById('authedGreeting').disabled = false;
    }
  });
  
};

/**
 * Handles the auth flow, with the given value for immediate mode.
 * @param {boolean} mode Whether or not to use immediate mode.
 * @param {Function} callback Callback to call on completion.
 */
google.devrel.samples.hello.signin = function(mode, callback) {
  gapi.auth.authorize({client_id: google.devrel.samples.hello.CLIENT_ID,
      scope: google.devrel.samples.hello.SCOPES, immediate: mode},
      callback);
};

/**
 * Presents the user with the authorization popup.
 */
google.devrel.samples.hello.auth = function() {
  if (!google.devrel.samples.hello.signedIn) {
    google.devrel.samples.hello.signin(false,
        google.devrel.samples.hello.userAuthed);
  } else {
    google.devrel.samples.hello.signedIn = false;
    document.getElementById('signinButton').innerHTML = 'Sign in';
    document.getElementById('authedGreeting').disabled = true;
  }
};

/**
 * Prints a greeting to the greeting log.
 * param {Object} greeting Greeting to print.
 */
google.devrel.samples.hello.print = function(asteroid) {
	if(!$('#AsteroidTable tr > td:contains(' + asteroid.name + ')').length)
	{
		var table = document.getElementById("AsteroidTable");
	  	var newRow = table.insertRow(-1);
	  	var cell1 = newRow.insertCell(0);
	  	var cell2 = newRow.insertCell(1);
	  	var cell3 = newRow.insertCell(2);
	  	var cell4 = newRow.insertCell(3);
	  	cell1.innerHTML = asteroid.name;
	  	cell2.innerHTML = asteroid.diameter;
	  	cell3.innerHTML = asteroid.dimension[0] + " x " + asteroid.dimension[1] + " x " + asteroid.dimension[2];
	  	cell4.innerHTML = asteroid.meanDFromSun;
	}
  	
};

/**
 * Gets a numbered greeting via the API.
 * @param {string} id ID of the greeting.
 */
google.devrel.samples.hello.getGreeting = function(id) {
  gapi.client.helloworld.greetings.getGreeting({'id': id}).execute(
      function(resp) {
        if (!resp.code) {
          google.devrel.samples.hello.print(resp);
        } else {
          window.alert(resp.message);
        }
      });
};

/**
 * Lists greetings via the API.
 */
google.devrel.samples.hello.listAsteroids = function() {
  gapi.client.helloworld.asteroidList.listAsteroids().execute(
      function(resp) {
        if (!resp.code) {
          resp.items = resp.items || [];
          for (var i = 0; i < resp.items.length; i++) {
            google.devrel.samples.hello.print(resp.items[i]);
          }
        }
      });
};

/**
 * Gets a greeting a specified number of times.
 * @param {string} greeting Greeting to repeat.
 * @param {string} count Number of times to repeat it.
 */
google.devrel.samples.hello.multiplyGreeting = function(
    greeting, times) {
  gapi.client.helloworld.greetings.multiply({
      'message': greeting,
      'times': times
    }).execute(function(resp) {
      if (!resp.code) {
        google.devrel.samples.hello.print(resp);
      }
    });
};

google.devrel.samples.hello.addAsteroid = function(asteroidName, asteroidDiam, asteroidLength, 
													asteroidWidth, asteroidHeight, asteroidDist)
{
	gapi.client.helloworld.asteroidList.addAsteroid({
		'asteroidName': asteroidName,
		'diameter': asteroidDiam,
		'dimensionL': asteroidLength,
		'dimensionW': asteroidWidth,
		'dimensionH': asteroidHeight,
		'meanDFromSun': asteroidDist
	}).execute(function(resp)
		{
			if(!resp.code)
			{
				google.devrel.samples.hello.print(resp);
			}
		});
};

/**
 * Greets the current user via the API.
 */
google.devrel.samples.hello.authedGreeting = function(id) {
  gapi.client.helloworld.greetings.authed().execute(
      function(resp) {
        google.devrel.samples.hello.print(resp);
      });
};

/**
 * Enables the button callbacks in the UI.
 */
google.devrel.samples.hello.enableButtons = function() {
  document.getElementById('listAsteroids').onclick = function() {
    google.devrel.samples.hello.listAsteroids();
  }
  
  document.getElementById('addAsteroid').onclick = function() {
	  google.devrel.samples.hello.addAsteroid(
			  document.getElementById('asteroidName').value,
			  document.getElementById('asteroidDiam').value,
			  document.getElementById('asteroidLength').value,
			  document.getElementById('asteroidWidth').value,
			  document.getElementById('asteroidHeight').value,
			  document.getElementById('asteroidDist').value);
  }
};

/**
 * Initializes the application.
 * @param {string} apiRoot Root of the API's path.
 */
google.devrel.samples.hello.init = function(apiRoot) {
  // Loads the OAuth and helloworld APIs asynchronously, and triggers login
  // when they have completed.
  var apisToLoad;
  var callback = function() {
    if (--apisToLoad == 0) {
      google.devrel.samples.hello.enableButtons();
      google.devrel.samples.hello.signin(true,
          google.devrel.samples.hello.userAuthed);
      google.devrel.samples.hello.listAsteroids();
    }
  }

  apisToLoad = 2; // must match number of calls to gapi.client.load()
  gapi.client.load('helloworld', 'v1', callback, apiRoot);
  gapi.client.load('oauth2', 'v2', callback);
  
  
};
