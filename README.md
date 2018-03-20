# GlobeControllerServer 

This software was created to control relays with a Raspberry Pi for the Lancaster Academy for the Preforming Arts' production of The Lion, The Witch, and the Wardrobe     

It allows remote control with the GlobeControllerClient over a network connection on port 2018.     

This software uses the PI4J project which is licensed under the LGPLv3. A copy of this license can be found at http://pi4j.com/license.html.     

Known issues:     
The server will not shutdown unless the clients are disconnected properly with the DISCONNECT command.     
The relay GPIO pin locations are currently not configurable.      
The server is vulnerable to a Denial of Service attack as is it will continue to accept clients forever.     
There is no built in rate limiting for the relays. This may cause the relays to wear out prematurely.     
Class name LED and LEDState are confusing. Probably should be changed to SimplePin and SimplePinState or similar.      