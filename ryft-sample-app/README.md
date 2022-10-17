# Ryft Sample App

Sample application to demonstrate integrating the Ryft Android SDK.

Please note that the inputs to the drop-in UI in this sample app are provided via text input or hard-coded for demonstration purposes.
When you are integrating this SDK into your application, these inputs should be provided as follows:

- Public API Key: this should be passed in as an build config field
- Payment Session Client Secret: this should be passed in from your back-end after the payment session is created
- (Optional) Sub Account ID: this should be passed in from your back-end where you retrieve data for the sub-merchant(s) you are taking payments on behalf of
- (Optional) Pay Button Title: the title you wish to display on the pay button when the user is taking payment
- (Optional) Usage: this should be set depending on where you call the drop-in (e.g. use 'Payment' when in checkout, use 'SetupCard' when saving cards outside of checkout)
- (Optional) Google Pay Merchant Name: this is the name of your business
- (Optional) Google Pay Merchant country code: this is the ISO 3166-1 alpha-2 country code of your business