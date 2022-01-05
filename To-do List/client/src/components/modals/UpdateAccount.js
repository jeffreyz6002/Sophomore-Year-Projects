import React, { useState } 	from 'react';
import { REGISTER, UPDATEACCOUNT }			from '../../cache/mutations';
import { useMutation }    	from '@apollo/client';
import { GET_DB_USER } 		from '../../cache/queries';
import { useQuery } 		from '@apollo/client';

import { WModal, WMHeader, WMMain, WMFooter, WButton, WInput, WRow, WCol } from 'wt-frontend';
import { use } from 'passport';

const UpdateAccount = (props) => {
	const [input, setInput] = useState({ name: '', email: '', password: '' });
	const [loading, toggleLoading] = useState(false);
	const [UpdateAccount] = useMutation(UPDATEACCOUNT);
	const [isVisible, setShowUpdate] = useState(true);

    var user = null;
	const {error, data, refetch } = useQuery(GET_DB_USER);
    if(data) { user = data.getCurrentUser; }
    console.log(user)

	const updateInputN = (e) => {
		const { name, value } = e.target;
		const updated = { ...input, [name]: value };
		updated.name = " ";
		updated.name = user.name;
		setInput(updated);
	};

	const updateInputE = (e) => {
		const { name, value } = e.target;
		const updated = { ...input, [name]: value };
		updated.name = " ";
		updated.name = user.email;
		setInput(updated);
	};

	const updateInputP = (e) => {
		const { name, value } = e.target;
		const updated = { ...input, [name]: value };
		updated.name = " ";
		updated.name = user.email;
		setInput(updated);
	};

	const handleUpdateAccount = async (e) => {
		for (let field in input) {
			if (!input[field]) {
				alert('All fields must be filled out to register');
				return;
			}
		}
		const { loading, error, data } = await UpdateAccount({ variables: { ...input } });
		if (loading) { toggleLoading(true) };
		if (error) { return `Error: ${error.message}` };
		if (data) {
			if(data.register){
				if(data.register.email && data.register.name && data.register.password){
					toggleLoading(false);
					props.fetchUser();
					props.LoginLogout();
					props.setShowUpdate(false);
				}
			}
		};
	};

	const handleCloseModal = async () =>{
		props.setShowUpdate(false);
	}

	return (
		<WModal className="signup-modal" visible={isVisible}>
			<WMHeader className="modal-header-UA" onClose={() => props.setShowUpdate(false)}>
				Enter Updated Account Information
			</WMHeader>
			{
				loading ? <div />
					: <WMMain>
						<div className="modal-name-word"> Name: </div>
						<WInput 
							className="modal-input" onBlur={updateInputN} name="name" labelAnimation="up" 
							barAnimation="solid" labelText="Full Name" wType="outlined" inputType="text" 
						/>
						<div className="modal-spacer">&nbsp;</div>
						<div className="modal-email-word"> Email: </div>
						<WInput 
							className="modal-input" onBlur={updateInputE} name="email" labelAnimation="up" 
							barAnimation="solid" labelText="Email Address" wType="outlined" inputType="text" 
						/>
						<div className="modal-spacer">&nbsp;</div>
						<div className="modal-password-word"> Password: </div>
						<WInput 
							className="modal-input" onBlur={updateInputP} name="password" labelAnimation="up" 
							barAnimation="solid" labelText="Password" wType="outlined" inputType="password" 
						/>
					</WMMain>
			}
			<WMFooter className="main-login-modal-footer"> 
				<WButton className="modal-button modal-button-UA" onClick={handleUpdateAccount} clickAnimation="ripple-light" hoverAnimation="darken" shape="rounded" alignment="left">
					Update
				</WButton>
				<WButton className="modal-button modal-button-cancel-UA" onClick={handleCloseModal} clickAnimation="ripple-light" hoverAnimation="darken" shape="rounded" alignment="right">
					Cancel
				</WButton>
				</WMFooter>
		</WModal>
	);
}

export default UpdateAccount;