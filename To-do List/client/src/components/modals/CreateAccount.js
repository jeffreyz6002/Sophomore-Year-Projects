import React, { useState } 	from 'react';
import { REGISTER }			from '../../cache/mutations';
import { useMutation }    	from '@apollo/client';

import { WModal, WMHeader, WMMain, WMFooter, WButton, WInput, WRow, WCol } from 'wt-frontend';

const CreateAccount = (props) => {
	const [input, setInput] = useState({ name: '', email: '', password: '' });
	const [loading, toggleLoading] = useState(false);
	const [Register] = useMutation(REGISTER);
	const [isVisible, setShowCreate] = useState(true);

	
	const updateInput = (e) => {
		const { name, value } = e.target;
		const updated = { ...input, [name]: value };
		setInput(updated);
	};

	const handleCreateAccount = async (e) => {
		for (let field in input) {
			if (!input[field]) {
				alert('All fields must be filled out to register');
				return;
			}
		}
		const { loading, error, data } = await Register({ variables: { ...input } });
		if (loading) { toggleLoading(true) };
		if (error) { return `Error: ${error.message}` };
		if (data) {
			toggleLoading(false);
			if(data.register.email === 'already exists') {
				alert('User with that email already registered');
			}
			else {
				props.fetchUser();
			}
			props.LoginLogout();
			props.setShowCreate(false);
		};
	};

	const handleCloseModal = async () =>{
		props.setShowCreate(false);
	}

	return (
		<WModal className="signup-modal" visible={isVisible}>
			<WMHeader className="modal-header-CA" onClose={() => props.setShowCreate(false)}>
				Create A New Account
			</WMHeader>
			{
				loading ? <div />
					: <WMMain>
						<div className="modal-name-word"> Name: </div>
						<WInput 
							className="modal-input" onBlur={updateInput} name="name" labelAnimation="up" 
							barAnimation="solid" labelText="Full Name" wType="outlined" inputType="text" 
						/>
						<div className="modal-spacer">&nbsp;</div>
						<div className="modal-email-word"> Email: </div>
						<WInput 
							className="modal-input" onBlur={updateInput} name="email" labelAnimation="up" 
							barAnimation="solid" labelText="Email Address" wType="outlined" inputType="text" 
						/>
						<div className="modal-spacer">&nbsp;</div>
						<div className="modal-password-word"> Password: </div>
						<WInput 
							className="modal-input" onBlur={updateInput} name="password" labelAnimation="up" 
							barAnimation="solid" labelText="Password" wType="outlined" inputType="password" 
						/>
					</WMMain>
			}
			<WMFooter className="main-login-modal-footer"> 
				<WButton className="modal-button modal-button-CA" onClick={handleCreateAccount} clickAnimation="ripple-light" hoverAnimation="darken" shape="rounded" alignment="left">
					Create Account
				</WButton>
				<WButton className="modal-button modal-button-cancel-CA" onClick={handleCloseModal} clickAnimation="ripple-light" hoverAnimation="darken" shape="rounded" alignment="right">
					Cancel
				</WButton>
				</WMFooter>
		</WModal>
	);
}

export default CreateAccount;
