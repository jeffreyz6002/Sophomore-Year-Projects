import React from 'react';

import { WButton, WRow, WCol } from 'wt-frontend';

const TableHeader = (props) => {

    const buttonStyle = props.disabled ? ' table-header-button-disabled ' : 'table-header-button ';
    const clickDisabled = () => { };
    const clickDescription = () => {
        let sortByVar = "description";
        props.handleSortBy(sortByVar);
    };
    const clickDueDate = () => {
        let sortByVar = "due_date";
        props.handleSortBy(sortByVar);
    };
    const clickStatus = () => {
        let sortByVar = "completed";
        props.handleSortBy(sortByVar);
    };
    const clickAssignTo = () => {
        let sortByVar = "assigned_to";
        props.handleSortBy(sortByVar);
    }

    return (
        <WRow className="table-header">
            <WCol size="3">
                <WButton className='table-header-section' wType="texted" onClick={clickDescription}>Task</WButton>
            </WCol>

            <WCol size="2">
                <WButton className='table-header-section' wType="texted" onClick={clickDueDate}>Due Date</WButton>
            </WCol>

            <WCol size="2">
                <WButton className='table-header-section' wType="texted" onClick={clickStatus}>Status</WButton>
            </WCol>

            <WCol size="2">
                <WButton className='table-header-section' wType="texted" onClick={clickAssignTo}>Assigned To</WButton>
            </WCol>

            <WCol size="3">
                <div className="table-header-buttons">
                    <WButton onClick={props.hasTransactionToUndo ? props.undo : clickDisabled} wType="texted" shape="rounded" className={props.hasTransactionToUndo ? ' undo-redo' : ''} 
                    clickAnimation={props.hasTransactionToUndo ? 'ripple-light' : ''}>
                        <i className="material-icons">undo</i>
                    </WButton>
                    <WButton onClick={props.hasTransactionToRedo ? props.redo : clickDisabled} wType="texted" shape="rounded" className={props.hasTransactionToRedo ? ' undo-redo' : ''} 
                    clickAnimation={props.hasTransactionToRedo ? 'ripple-light' : ''}>
                        <i className="material-icons">redo</i>
                    </WButton>
                    <WButton onClick={props.disabled ? clickDisabled : props.addItem} wType="texted" className={`${buttonStyle}`}>
                        <i className="material-icons">add_box</i>
                    </WButton>    
                    <WButton onClick={props.disabled ? clickDisabled : props.setShowDelete} wType="texted" className={`${buttonStyle}`}>
                        <i className="material-icons">delete_outline</i>
                    </WButton>
                    <WButton onClick={props.disabled ? clickDisabled : () => props.handleClickClose()} wType="texted" className={`${buttonStyle}`}>
                        <i className="material-icons">close</i>
                    </WButton>
                </div>
            </WCol>

        </WRow>
    );
};

export default TableHeader;

//clickAnimation={props.hasTransactionToRedo ? 'ripple-light' : ''}