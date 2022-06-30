# Ryft Sample App

Sample application to demonstrate integrating the Ryft Android SDK.

Please note that the inputs to the drop-in UI in this sample app are provided via text input or hard-coded for demonstration purposes.
When you are integrating this SDK into your application, these inputs should be provided as follows:

- Public API Key: this should be passed in as an build config field
- Payment Session Client Secret: this should be passed in from your back-end after the payment session is created
- (Optional) Sub Account ID: this should be passed in from your back-end where you retrieve data for the sub-merchant(s) you are taking payments on behalf of
- Google Pay Merchant Name: this is the name of your business
- Google Pay Merchant country code: this is the ISO 3166-1 alpha-2 country code of your business