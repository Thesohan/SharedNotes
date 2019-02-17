// imSports firebase-functions module
const functions = require('firebase-functions');
// imports firebase-admin module
const admin = require('firebase-admin');


// Initialize your firebase-admin app instance. It helps to trigger firebase events

admin.initializeApp(functions.config().firebase);


// Create the function that sends the push notification
/* Listens for new messages added to /messages/:pushId and sends a notification to subscribed users */
// {someVariable} it is called wildcard values
// onCreate method takes two arguments, snapshot is ref.
exports.finalPdfRequestsPushNotification = functions.database.ref('{collegeName}/PdfRequests/{schoolName}/{pushId}')
.onCreate( (snapshot,context) => {
console.log('Push notification event triggered for pdf requests');

const collegeName=context.params.collegeName;
 const schoolName=context.params.schoolName;//maybe both should be same
 console.log('here is m now '+collegeName+schoolName)
  // Grab the current value of what was written to the Realtime Database
     var valueObject = snapshot.val();
/* Create a notification and data payload. They contain the notification information, and message to be sent respectively */
    const payload = {
        notification: {
            title: 'There is a new notice request in '+schoolName+" (pdf section)",
            body: valueObject.title,
            sound: "default"
         }
         //,
        // data: {
        //     title: valueObject.title,
        //     schoolName: schoolName
        // }
    };
/* Create an options object that contains the time to live for the notification and the priority. */
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24 //24 hours
    };
    //subscribe the token when onToken method called in myFirebaseMessaging class
return admin.messaging().sendToTopic(collegeName+"AdminToken", payload, options);
});