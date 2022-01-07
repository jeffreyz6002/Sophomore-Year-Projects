import React                    from 'react';
import { WButton, WRow, WCol }  from 'wt-frontend';

const SidebarHeader = (props) => {
    const clickDisabled = () => {
        console.log("1. "+props.activeList)
     };

    return (
        <WRow className='sidebar-header'>
            <WCol size="7">
                <WButton wType="texted" hoverAnimation="text-primary" className='sidebar-header-name'>
                    Todolists
                </WButton>
            </WCol>

            <WCol size="5">
                {
                    props.auth && <div className="sidebar-options">
                        <WButton className="sidebar-buttons" onClick={props.activeList._id !== undefined? clickDisabled : props.createNewList} 
                        clickAnimation={props.activeList._id !== undefined? "" :"ripple-light"} shape="rounded" color="primary">
                            {
                            props.activeList._id !== undefined ? <div></div>:
                            <i className="material-icons">add</i>
                            }
                        </WButton>
                    </div>
                }
            </WCol>

        </WRow>

    );
};

export default SidebarHeader;