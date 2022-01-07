import React, { useState } 	from 'react';
import { LOGIN } 			from '../../cache/mutations';
import { useMutation }    	from '@apollo/client';

import { WModal, WMHeader, WMMain, WMFooter, WButton, WInput } from 'wt-frontend';

const Login = (props) => {
	const [input, setInput] = useState({ email: '', password: '' });
	const [loading, toggleLoading] = useState(false);
	const [showErr, displayErrorMsg] = useState(false);
	const errorMsg = "Email/Password not found.";
	const [Login] = useMutation(LOGIN);
	const [isVisible, setShowLogin] = useState(true);
	const [showLogin] = useState(true);

	const updateInput = (e) => {
		const { name, value } = e.target;
		const updated = { ...input, [name]: value };
		setInput(updated);
	}

	const handleLogin = async (e) => {

		const { loading, error, data } = await Login({ variables: { ...input } });
		if (loading) { toggleLoading(true) };
		if (data.login._id === null) {
			displayErrorMsg(true);
			return;
		}
		if (data) {
			props.fetchUser();
			props.refetchTodos();
			toggleLoading(false)
			props.setShowLogin(false)
		};
		props.LoginLogout();
	};

	const handleCloseModal = async () =>{
		props.setShowLogin(false);
	}


	return (
		<WModal className="login-modal" visible={showLogin} cover={true}>
			<WMHeader className="modal-header-login" onClose={() => props.setShowLogin(false)}>
				Login To Your Account
			</WMHeader>
			{
				loading ? <div />
					: <WMMain className="main-login-modal">
						<div className="modal-email"> 
						<div className="modal-email-word"> Email: </div>
						<WInput className="modal-input" onBlur={updateInput} name='email' labelAnimation="up" barAnimation="solid" labelText="Email Address" wType="outlined" inputType='text' />
						</div>
						<div className="modal-spacer">&nbsp;</div>
						
						<div className="modal-password"> 
						<div className="modal-password-word"> Password: </div>
						<WInput className="modal-input" onBlur={updateInput} name='password' labelAnimation="up" barAnimation="solid" labelText="Password" wType="outlined" inputType='password' />
						</div>
						{
							showErr ? <div className='modal-error'>
								{errorMsg}
							</div>
								: <div className='modal-error'>&nbsp;</div>
						}
				</WMMain>
			}

			<WMFooter className="main-login-modal-footer"> 
				<WButton className="modal-button modal-button-login" onClick={handleLogin} clickAnimation="ripple-light" hoverAnimation="darken" shape="rounded" alignment="left">
					Login
				</WButton>

				<WButton className="modal-button modal-button-cancel-L" onClick={handleCloseModal} clickAnimation="ripple-light" hoverAnimation="darken" shape="rounded" alignment="right">
					Cancel
				</WButton>
				</WMFooter>

		</WModal>
	);
}

export default Login;