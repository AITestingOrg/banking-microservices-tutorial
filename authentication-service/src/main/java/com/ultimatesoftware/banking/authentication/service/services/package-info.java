/**
 * Service classes create and login user request received from the UserController
 * <p>
 * These classes receives requests from the controller(s). They create User from
 * provided credentials which cannot be empty and store new user in the persistence
 * layer. Upon receipt of login request Users submits complete object and service
 * will search database for a complete match, if match is found JWT is generated
 * and issued.
 *
 * @version 1.0
 */
package com.ultimatesoftware.banking.authentication.service.services;
